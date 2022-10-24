package com.android.internal.telephony;

interface ITelephony{
    //挂断电话
    boolean endCall();

    //接听电话
    void answerRingingCall();
}