#ifndef DND_CHARACTER_BUILDER_CPPPARSER_RACEPARSER_H
#define DND_CHARACTER_BUILDER_CPPPARSER_RACEPARSER_H
#include <string>
#include <vector>

namespace cpp_parser {
    class RaceParser {
    public:
        static std::vector<std::string> ParseRace(std::string input);
    };
}

#endif //DND_CHARACTER_BUILDER_CPPPARSER_RACEPARSER_H
