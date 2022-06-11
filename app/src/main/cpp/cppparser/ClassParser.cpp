#include "ClassParser.h"

#include <boost/json/error.hpp>
#include <boost/json/parse.hpp.>
#include <boost/json/serialize.hpp>
#include <iostream>

namespace
{
    constexpr char CLASS[] = "class";
    constexpr char NAME[] = "name";
}

std::string cpp_parser::ClassParser::ParseClass(const std::string& input)
{
    boost::json::error_code errorCode {};
    const auto jsonValue = boost::json::parse(input, errorCode);
    if (errorCode)
    {
        std::cerr << errorCode.message();
        return "";
    }
    try {
        const auto& classJsonObject = jsonValue.as_object().at(CLASS).as_array().at(0).as_object();
        boost::json::object outputJsonObject;
        outputJsonObject.emplace(NAME, classJsonObject.at(NAME).as_string());
        return boost::json::serialize(outputJsonObject);
    }
    catch (const std::exception& exception) {
        std::cerr << exception.what() << std::endl;
        return "";
    }
}

std::vector <std::string> cpp_parser::ClassParser::ParseIndexFile(const std::string& input)
{
    boost::json::error_code errorCode {};
    const auto jsonValue = boost::json::parse(input, errorCode);
    if (errorCode)
    {
        std::cerr << errorCode.message();
        return {};
    }
    const auto& jsonObject = jsonValue.as_object();

    std::vector<std::string> fileNames;
    fileNames.reserve(jsonObject.size());
    try {
        for (const auto& item : jsonObject)
        {
            fileNames.push_back(item.value().as_string().c_str());
        }
    }
    catch (const std::exception& exception) {
        std::cerr << exception.what() << std::endl;
    }
    return fileNames;
}
