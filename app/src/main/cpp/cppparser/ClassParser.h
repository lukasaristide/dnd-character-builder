#ifndef DND_CHARACTER_BUILDER_CLASSPARSER_H
#define DND_CHARACTER_BUILDER_CLASSPARSER_H

#include <string>
#include <vector>

namespace cpp_parser {
    class ClassParser {
    public:
        static std::string ParseClass(const std::string& input);
        static std::vector<std::string> ParseIndexFile(const std::string& input);
    };
}

#endif //DND_CHARACTER_BUILDER_CLASSPARSER_H
