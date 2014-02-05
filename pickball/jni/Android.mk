LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
# Here we give our module name and source file(s)
LOCAL_MODULE    := pickball

LOCAL_SRC_FILES := pickballjni.c 

    
LOCAL_LDLIBS := -llog  

include $(BUILD_SHARED_LIBRARY)