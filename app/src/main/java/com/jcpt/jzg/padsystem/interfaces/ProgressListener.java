package com.jcpt.jzg.padsystem.interfaces;

public interface ProgressListener {
        void onProgress(long hasWrittenLen, long totalLen, boolean hasFinish);
    }
