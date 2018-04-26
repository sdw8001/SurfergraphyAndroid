package com.surfergraphy.surf.login;

import android.app.Application;
import android.text.TextUtils;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.surfergraphy.surf.base.viewmodel.BaseViewModel;
import com.surfergraphy.surf.login.data.RequestModel_MemberInfo;
import com.surfergraphy.surf.login.data.repositories.LoginRepository;

import static com.surfergraphy.surf.utils.RealmUtils.loginMemberModel;

public class ViewModel_Login extends BaseViewModel {

    private LoginRepository loginRepository;

    public ViewModel_Login(Application application) {
        super(application);
        loginRepository = new LoginRepository(realm);
    }

    public void loginMember(final int actionCode, final RequestModel_MemberInfo requestModel) {
        loginRepository.syncExpiredPhotos();
        loginRepository.loginMember(actionCode, requestModel);
    }

    public void logoutAccount(String loginType) {
        if (TextUtils.equals(loginType, "G")) {
            FirebaseAuth.getInstance().signOut();
            loginMemberModel(realm).removeLoginMember();
        } else if (TextUtils.equals(loginType, "F")) {
            LoginManager.getInstance().logOut();
            loginMemberModel(realm).removeLoginMember();
        }
    }

}
