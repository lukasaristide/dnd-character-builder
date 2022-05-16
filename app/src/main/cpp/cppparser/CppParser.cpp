// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("dndcharacterbuilder");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("dndcharacterbuilder")
//      }
//    }
#include "jni.h"
#include "RaceParser.h"
#include <string>
#include <boost/json/src.hpp>

extern "C"
JNIEXPORT jobjectArray JNICALL
Java_com_dndcharacterbuilder_jsonloader_CppParserKt_GetRaces(JNIEnv *env, jclass clazz, jstring url) {
    const char* urlCString = env->GetStringUTFChars(url, nullptr);
    const auto parsedJsons = cpp_parser::RaceParser().ParseRace(
            {urlCString, static_cast<size_t>(env->GetStringLength(url))});
    env->ReleaseStringUTFChars(url, urlCString);

    jobjectArray arr = env->NewObjectArray(parsedJsons.size(), env->GetObjectClass(url), env->NewStringUTF(""));

    for (uint32_t i = 0; i < parsedJsons.size(); ++i)
    {
        env->SetObjectArrayElement(arr, i, env->NewStringUTF(parsedJsons[i].c_str()));
    }
    return arr;
}