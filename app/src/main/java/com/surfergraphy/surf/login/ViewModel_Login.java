package com.surfergraphy.surf.login;

import android.app.Application;

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
        loginRepository.loginMember(actionCode, requestModel);
    }

    public void logoutAccount() {
        FirebaseAuth.getInstance().signOut();
        loginMemberModel(realm).removeLoginMember();
    }

}
