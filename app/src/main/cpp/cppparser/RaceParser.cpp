#include "RaceParser.h"

#include <boost/json/error.hpp>
#include <boost/json/parse.hpp.>
#include <boost/json/serialize.hpp>
#include <iostream>
#include <vector>
#include <stack>

#include "Keys.h"

std::vector<std::string> cpp_parser::RaceParser::ParseRace(std::string input) const {
    {
        std::string processed;
        std::stack<char> brackets;
        const auto indexBegin = input.find(RACE);

        for (auto index = input.find("[", indexBegin); index < input.size(); ++index)
        {
            const char c = input[index];
            switch (c) {
                case '[':
                    brackets.push(c);
                    break;
                case ']':
                    if (!brackets.empty()) {
                        brackets.pop();
                        break;
                    }
                    else
                        return {};
                case '\t':
                    continue;
                default:
                    break;
            }
            processed += c;
            if (brackets.empty())
                break;
        }
        input.swap(processed);
    }

    boost::json::error_code errorCode {};
    const auto jsonValue = boost::json::parse(input, errorCode);

    if (errorCode)
    {
        std::cerr << errorCode.message() << std::endl;
    }
    std::vector<std::string> names;
    try {
        const auto& racesJsonArray = jsonValue.as_array();
        names.reserve(racesJsonArray.size());
        for (const auto& jsonRace : racesJsonArray)
        {
            const auto& jsonObjectRace = jsonRace.as_object();
            const auto& name = jsonObjectRace.at(NAME).as_string();
            const auto& source = jsonObjectRace.at(SOURCE).as_string();
            names.push_back(name.c_str() + std::string("-") + source.c_str());
        }
        for (auto& name : names)
        {
            boost::json::object raceObj;
            raceObj.emplace(NAME, name);
            name = boost::json::serialize(raceObj);
        }
    }
    catch (const std::exception& exception) {
        std::cerr << exception.what() << std::endl;
    }


    return names;
}