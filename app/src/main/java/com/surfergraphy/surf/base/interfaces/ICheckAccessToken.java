package com.surfergraphy.surf.base.interfaces;

import com.google.firebase.auth.FirebaseAuth;

public interface ICheckAccessToken {
    void initFirebaseAuth();
    FirebaseAuth getFirebaseAuth();
    void checkAccessToken();
}
