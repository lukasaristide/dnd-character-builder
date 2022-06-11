#include "jni.h"
#include "RaceParser.h"
#include "ClassParser.h"
#include <string>
#include <boost/json/src.hpp>

namespace
{
    jstring CppStringToJString(JNIEnv *env, const std::string& string)
    {
        return env->NewStringUTF(string.c_str());
    }
    jobjectArray CppVectorOfStringsToJNIStringArray(JNIEnv *env,
                                                    const std::vector<std::string>& vector)
    {
        jobjectArray arr = env->NewObjectArray(vector.size(),
                                               env->FindClass("java/lang/String"),
                                               env->NewStringUTF(""));
        for (uint32_t index = 0; index < vector.size(); ++index)
        {
            env->SetObjectArrayElement(arr, index, CppStringToJString(env, vector[index]));
        }
        return arr;
    }
}

extern "C"
JNIEXPORT jobjectArray JNICALL
Java_com_dndcharacterbuilder_jsonloader_CppParserKt_GetRaces(JNIEnv *env, jclass clazz,
                                                             jstring json)
{
    const char* jsonCString = env->GetStringUTFChars(json, nullptr);
    const auto parsedJsons = cpp_parser::RaceParser::ParseRace(
            {jsonCString, static_cast<size_t>(env->GetStringLength(json))});
    env->ReleaseStringUTFChars(json, jsonCString);

    return CppVectorOfStringsToJNIStringArray(env, parsedJsons);
}
extern "C"
JNIEXPORT jobjectArray JNICALL
Java_com_dndcharacterbuilder_jsonloader_CppParserKt_GetClassFileNamesFromIndexFile(JNIEnv *env,
                                                                                   jclass clazz,
                                                                                   jstring json)
{
    const char* jsonCString = env->GetStringUTFChars(json, nullptr);
    const auto parsedFileNames = cpp_parser::ClassParser::ParseIndexFile(jsonCString);
    env->ReleaseStringUTFChars(json, jsonCString);

    return CppVectorOfStringsToJNIStringArray(env, parsedFileNames);
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_dndcharacterbuilder_jsonloader_CppParserKt_GetClass(JNIEnv *env, jclass clazz, jstring json) {
    const char* jsonCString = env->GetStringUTFChars(json, nullptr);
    const auto parsedClass = cpp_parser::ClassParser::ParseClass(jsonCString);
    env->ReleaseStringUTFChars(json, jsonCString);
    return CppStringToJString(env, parsedClass);
}