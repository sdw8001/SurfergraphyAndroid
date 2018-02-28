package com.surfergraphy.surf.base.helper;

import android.app.Activity;
import android.content.Intent;

import com.surfergraphy.surf.base.util.IabHelper;
import com.surfergraphy.surf.base.util.IabResult;
import com.surfergraphy.surf.base.util.Inventory;
import com.surfergraphy.surf.base.util.Purchase;
import com.surfergraphy.surf.wavepurchase.data.repositories.WavePurchasePhotoRepository;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.surfergraphy.surf.base.util.IabHelper.IABHELPER_USER_CANCELLED;

public class InAppBillingHelper {

    public static final String TAG = "InAppBillingHelper";

    // onActivityResult에서 받을 requestCode.
    public static final int REQUEST_CODE = 1001;
    // publicKey 개발자콘솔에서 앱을 생성후 얻을 수 있다.
    private String mPublicKey;

    // 테스트를 하기 위한 테스트용 productId(SKU)
    private static final String TEST_SKU = "android.test.purchased";
    // 구매되고 소진되지 않은 아이템을 캐시해놓을 변수.
    private ArrayList<String> mOwnedItems = new ArrayList<>();

    // 가져온 util클래스에서 제공하는 클래스. 아이템리스트를 갖고 있다.
    private Inventory mInventory;

    // 가져온 util클래스의 실제 헬퍼클래스
    private IabHelper mHelper;

    // 자체적으로 서버와 통신할 서비스 클래스. (여기선 구현은 생략)
//    private ItemService mItemService;

    private Activity mActivity;

    // 테스트 여부인지.
    private boolean mIsTest;

    // 실제 우리가 알고있는 아이템목록. (어플리케이션 서버로부터 받아오면됨.)
    public List<String> mItems = new ArrayList<>();

    // 로드 이후 호출될 리스너.
    public interface InventoryLoadListener {
        void onBefore();
        void onSuccess(Inventory inventory);
        void onFail(IabResult result);
    }

    // 구매 이후 호출될 리스너
    public interface PurchaseFinishedListener {
        void onSuccess(Purchase purchase);
        void onFail(IabResult result);
    }

    public void init(Activity activity) {
        mActivity = activity;
//        mItemService = new ItemService(activity);
        mIsTest = false;
    }
    // 공개키가 없는 생성자.
    public InAppBillingHelper(Activity activity) {
        mPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmj+SwFtj8x+QdWDTw0z55/PSqqa02dE1kzxXX15nub4i8m3kOC2/tHv7A3IxJiHPgX+39Yq/wDve9NfY+KthSK6smXPSas9+uKmqulVHet0n3SaIstWwKqssKBhwFOi2m1w3NGadAYrG2qbINCyPKCma0LIU83x2ORSsWTahJnXCk7ZzTapD/t4yaTvHs0la1J5AyE03dzieICIytSBHkBzA2YK2fdAzxDELeE8F7Qdu5OpiUczM6WUtYHQHWTWqGG5Qssy8eMlMF0/khEsR8ZLRfOzl6aQCaOsfyqUS/diqix9L7q1AQCliKAyFoyqipcRzEnK92uNj3duCz+1SXwIDAQAB";
        init(activity);
    }
    // 공개키가 있는 생성자.
    public InAppBillingHelper(Activity activity, String publicKey) {
        mPublicKey = publicKey;
        init(activity);
    }
    // 테스트 여부의 세터.
    public void setTest(boolean isTest) {
        mIsTest = isTest;
    }

    public void startSetup(ArrayList<String> items, final InventoryLoadListener listener) {
        // before() 를 호출함 으로서 로딩바를 보여주는 등의 액션을 취할 수 있다.
        listener.onBefore();
        // 실제 구글 서버에 요청할 sku리스트.
        mItems = items;
        // 서버에서 테스트 sku목록까지 넣어놓았었다면 제외.
        for (int i = 0; i < mItems.size(); ++i) {
            if (mItems.get(i).equals(TEST_SKU)) {
                mItems.remove(mItems.get(i));
                break;
            }
        }
        // 실제 util에서 가져온 헬퍼생성.
        mHelper = new IabHelper(mActivity, mPublicKey);
        // startSetup함수를 호출함으로서 현재 통신가능여부를 확인하고, 정상적으로 커넥션이 이루어졌는지 확인할 수 있다.
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    listener.onFail(result);
                } else {
                    // 성공적으로 연결이 되었다면 이제 실질적인 아이템을 로드해야한다.
                    loadItemInventory(listener);
                }
            }
        });
    }

    // 아이템을 로드하는 메소드
    private void loadItemInventory(final InventoryLoadListener listener) {
        // 실제 아이템목록을 받아올 수 있음.
        // mItems에 올바른 값이 할당되어야 함.
        // 만약 mItems가 null값이라면 queryInventoryAsync메소드는 콜백이 호출될때 성공했다고 나오지만
        // 어떠한 아이템 리스트 값도 받아올 수 없음.
        mHelper.queryInventoryAsync(true, mItems, new IabHelper.QueryInventoryFinishedListener() {
            @Override
            public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                // IabResult 값에는 다양한 에러코드및 성공여부를 담고 있음.
                if (result.isSuccess()) {
                    // 멤버변수로 받아온 아이템리스트를 갖고 있는 inventory를 담음.
                    mInventory = inv;
                    // mInventory에는 구매는 했지만 소진되지 않은 값을 얻어올 수 있음.
                    // 따라서 새롭게 받아온 인벤토리를 통해 갱신하기 위해 클리어.
                    mOwnedItems.clear();
                    // 이제 mItems의 sku값을을 갖고 inv에 조회하여 소진되지 않은 아이템 목록을 담음.
                    for (String sku : mItems) {
                        if (inv.hasPurchase(sku)) {
                            mOwnedItems.add(sku);
                        }
                    }
                    // 테스트sku는 가끔 소진되지 않을 때가 있음. 이럴땐 무조건 헬퍼가 수행될 때 소진시킴.
                    if (mInventory.hasPurchase(TEST_SKU)) {
                        // consumeItem에 첫번째 인자는 해당 sku이며, 두번째 인자는 PurchaseInfo 인스턴스가 됨. 이후에 다시 설명.
                        consumeItem(TEST_SKU, null);
                    }
                    listener.onSuccess(inv);
                } else {
                    listener.onFail(result);
                    /*String json = "{\"orderId\":\"GPA.3318-4762-3315-66836\",\"packageName\":\"com.surfergraphy.surf\",\"productId\":\"wave15\",\"purchaseTime\":1519799996289,\"purchaseState\":0,\"purchaseToken\":\"imlpbfglpiclgkimdfklcpna.AO-J1OxH5UPm4fBV24_F9hx6C9ZuBRCtIIBdsUeWD8IpltbDkUGcK-7fzManQnjecDAKoQ7vgig86igLv5wCA5nS8GX9v69ioxyXPTLsF2BMtwYcPawEYVM\"}";
                    String signature = "RyPA9KmW0ln5AnPCWN5as5mWw8UN98KxkSq5xK3vDsG7DsUXw7aWnk/v+r0/bMp6QmzZt5DDVRrwIU9CetBed5i1vp+B3DwPLTlxZs8ZjV47SWndzTooyRS240eZohBbLhufwUEXHymBlcnHL18QoesnPeWWzaP7PQCCt1k50KZfC/2PuIZ15ZG2Nybkj78f7vzJzQliR0CCQ4sllcd4rdKzw+UBkBTj+KRgotZpZK+WmAadGCQg7VB2c3XTqiFBXVGGlW+1IXkdWwp/eYP2TDFTQ3M5eSrRlg33ib1W5cUQdOqoKlLAy9keI/yFZTOa8OsXs0d0dCCs3Vr6tGg7Aw==";
                    Purchase purchase = null;
                    try {
                        purchase = new Purchase("inapp", json, signature);
                        consumeItem("wave15", purchase);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                }
            }
        });
    }

    // 실제 구매 (인앱결제)
    public void purchaseItem(final String userId, final WavePurchasePhotoRepository repository, final String sku, final PurchaseFinishedListener listener) {
        // 만약 test가 true라면 테스트SKU를 갖고 구매요청을 한다.
        final String refinedSKU = mIsTest ? TEST_SKU : sku;
        // 구매후 소진되지 않은 아이템이라면 다시 구매할 수 없다.
        if (!mOwnedItems.contains(refinedSKU)) {
            // 실제 구매 & 결제요청
            // 해당 메소드를 호출하면 내부적으로 팝업창형태의 결제창이 나온다.
            mHelper.launchPurchaseFlow(mActivity, refinedSKU, REQUEST_CODE, new IabHelper.OnIabPurchaseFinishedListener() {
                @Override
                public void onIabPurchaseFinished(IabResult result, final Purchase info) {
                    if (result.isSuccess()) {
                        // 구매되고 소진되지 않은 목록에 캐시.
                        mOwnedItems.add(info.getSku());

                        // 실제 어플리케이션 서버에서 소진되도록 호출.
                        consumeItemForServer(userId, repository, info);
                        listener.onSuccess(info);
                    } else {
                        if (result.getResponse() == 7) {
                            // 구매가 실패하였고 이유가 아직 소진되지 않은 것이라면
                            // 소진을 위해 서버에 요청.
                            consumeAllItemsForServer(userId, repository);
                        }
                        if (result.getResponse() == IABHELPER_USER_CANCELLED)
                            CommonHelper.showMessageOK(mActivity, "구매를 취소했습니다.");
//                        ErrorHandler.handleLocalError(mActivity, LocalErrorCode.ITEM_PURCHASE_ERROR);
                        listener.onFail(result);
                    }
                }
            });
        } else {
            // 이미 구매한 아이템이라면 모두 소진시킨다.
            consumeAllItemsForServer(userId, repository);
        }
    }

    // 외부 액티비티나 프래그먼트의 onActivityResult함수에서 호출해야 한다.
    // 해당 메소드가 호출되지 않으면 정상적으로 프로세스가 끝나지 않는다.
    // mHelper.handleActivityResult 내부에서 onIabPurchaseFinished메소드가 호출된다.
    public boolean onActivityResult(int requestCode, int resultCode, Intent data){
        return mHelper.handleActivityResult(requestCode, resultCode, data);
    }

    // 실제 어플리케이션 서버에 아이템 구매요청을 한다.
    private void consumeItemForServer(final String userId, final WavePurchasePhotoRepository repository, final Purchase purchase) {
        final String refinedSKU = mIsTest ? TEST_SKU : purchase.getSku();

        // Purchase 정보를 내부적으로 서버에 저장
        repository.addPurchase(new WavePurchasePhotoRepository.ResponseCallback() {
            @Override
            public void onSuccess() {
                if (mActivity != null) {
//                    FullScreenProgressBar.hide();
                }
                // 통신이 성공하면 실제 소진을 시킨다.
                // 여기서 두번째 인자로 purchase 인스턴스를 준다.
                // 이때에는 mInventory가 갱신이 되어 있지 않는 경우가 종종있다.
                // 따라서 mInventory에서 purchase를 가져오는 것이다니고
                // 해당 구매 콜백으로 부터 가져온다.
                consumeItem(refinedSKU, purchase);
            }

            @Override
            public void onFail(String message) {

            }
        }, userId, purchase.getOrderId(), purchase.getItemType(), purchase.getPackageName(), purchase.getSku(), purchase.getPurchaseTime(), purchase.getPurchaseState(), purchase.getDeveloperPayload(), purchase.getToken(), purchase.getOriginalJson(), purchase.getSignature());
    }

    // 실제 소진 처리.
    public void consumeItem(String sku, Purchase purchase) {
        final String refinedSKU = mIsTest ? TEST_SKU : sku;
        if (purchase != null || mInventory.hasPurchase(refinedSKU)) {
            // purchase가 있다면 해당 구매 인스턴스를 통해 처리하고, 그게 아니라면 mInventory를 통해처리
            // 보통 구매성공, 소진실패일 경우일때 mInventory.hasPurchase(refinedSKU)를 통해서 가져오면 된다.
            Purchase refiendPurchase = (purchase == null) ? mInventory.getPurchase(refinedSKU) : purchase;

            // 소진처리.
            mHelper.consumeAsync(refiendPurchase, new IabHelper.OnConsumeFinishedListener() {
                @Override
                public void onConsumeFinished(Purchase purchase, IabResult result) {
                    if (result.isSuccess()) {
                        // 소진에 성공하면 캐시된 값을 지운다.
                        mOwnedItems.remove(purchase.getSku());
                    } else {
//                        ErrorHandler.handleLocalError(mActivity, LocalErrorCode.ITEM_FATAL_ERROR);
                    }
                }
            });
        } else {
//            ErrorHandler.handleLocalError(mActivity, LocalErrorCode.ITEM_FATAL_ERROR);
        }
    }

    private static int sPurchaaseCount = 0;
    public void consumeAllItemsForServer(final String userId, final WavePurchasePhotoRepository repository) {
        if (mOwnedItems.size() > sPurchaaseCount && mOwnedItems.get(sPurchaaseCount) != null) {
            final String sku = mOwnedItems.get(sPurchaaseCount);
            Purchase purchase = mInventory.getPurchase(sku);
            if (purchase != null) {
                // Purchase 정보를 내부적으로 서버에 저장
                repository.addPurchase(new WavePurchasePhotoRepository.ResponseCallback() {
                    @Override
                    public void onSuccess() {
                        sPurchaaseCount++;
                        consumeItem(sku, null);
                        consumeAllItemsForServer(userId, repository);
                    }

                    @Override
                    public void onFail(String message) {

                    }
                },userId , purchase.getOrderId(), purchase.getItemType(), purchase.getPackageName(), purchase.getSku(), purchase.getPurchaseTime(), purchase.getPurchaseState(), purchase.getDeveloperPayload(), purchase.getToken(), purchase.getOriginalJson(), purchase.getSignature());
            }
            else {
                handleError();
            }
        }
        else {
            handleError();
        }
    }
    private void handleError() {
        sPurchaaseCount = 0;
        mOwnedItems.clear();
        if (mActivity != null) {
//            FullScreenProgressBar.hide();
        }
    }
}
