
cmake_minimum_required(VERSION 3.5)
set_property(GLOBAL PROPERTY USE_FOLDERS ON)
# Modules
list(APPEND CMAKE_MODULE_PATH "${CMAKE_CURRENT_SOURCE_DIR}/cmake/")
# Compiler detection (C++14, C++17, ...)
include(LIEFCompilerDetection)
# LIEF Project
# ============
project(LIEF VERSION ${LIEF_VERSION_MAJOR}.${LIEF_VERSION_MINOR}.${LIEF_VERSION_PATCH})
message(STATUS "${PROJECT_NAME} ${PROJECT_VERSION}")


include(CheckCXXCompilerFlag)
include(CheckCCompilerFlag)
option(LIEF_ELF            "Build LIEF with ELF module"                 ON)





file(READ "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/enums.inc"      LIEF_ELF_ENUMS)
file(READ "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/structures.inc" LIEF_ELF_STRUCTURES)

# LIEF Source definition
# ======================
set_source_files_properties(${mbedtls_src_crypto} PROPERTIES GENERATED TRUE)
set_source_files_properties(${mbedtls_src_x509} PROPERTIES GENERATED TRUE)
set_source_files_properties(${mbedtls_src_tls} PROPERTIES GENERATED TRUE)

set(LIEF_PRIVATE_INCLUDE_DIR)
set(LIEF_PUBLIC_INCLUDE_DIR)

set(LIEF_PUBLIC_INCLUDE_FILES)
set(LIEF_PRIVATE_INCLUDE_FILES)

set(LIBLIEF_SOURCE_FILES
        "${ELG_CC_PATH}"
        "${mbedtls_src_crypto}"
        "${mbedtls_src_x509}"
        "${mbedtls_src_tls}"
        "${LIBFUZZER_SRC_FILES}"
        "${CMAKE_CURRENT_SOURCE_DIR}/src/logging.cpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/src/exception.cpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/src/iostream.cpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/src/utils.cpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/src/Object.cpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/src/Object.tcc"
        "${CMAKE_CURRENT_SOURCE_DIR}/src/Visitor.cpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/src/BinaryStream/BinaryStream.cpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/src/BinaryStream/VectorStream.cpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/src/BinaryStream/Convert.cpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/src/visitors/hash.cpp")


# Grouping basic headers together
# ===============================
set(LIEF_INC_FILES
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/Abstract.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/exception.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/iostream.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/iterators.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/LIEF.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/logging.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/MachO.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/PE.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/types.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/utils.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/visibility.h"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/Object.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/Visitor.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/visitor_macros.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/BinaryStream/BinaryStream.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/BinaryStream/VectorStream.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/BinaryStream/Convert.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/src/frozen.hpp"
        )


set(LIEF_VISITOR_INCLUDE_FILES
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/hash.hpp")

set(LIEF_INCLUDE_FILES
        ${LIEF_INC_FILES}
        ${LIEF_VISITOR_INCLUDE_FILES}
        )


source_group("Source Files" FILES ${LIBLIEF_SOURCE_FILES})

source_group("Header Files\\visitors" FILES ${LIEF_VISITOR_INCLUDE_FILES})

add_library(LIB_LIEF  SHARED ${LIBLIEF_SOURCE_FILES} ${LIEF_INCLUDE_FILES})

configure_file(
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/enums.hpp.in"
        "${CMAKE_CURRENT_BINARY_DIR}/include/LIEF/ELF/enums.hpp"
        @ONLY
)

configure_file(
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/Structures.hpp.in"
        "${CMAKE_CURRENT_BINARY_DIR}/include/LIEF/ELF/Structures.hpp"
        @ONLY
)

set(LIEF_ELF_SRC
        "${CMAKE_CURRENT_LIST_DIR}/Convert.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/DynamicEntryArray.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/utils.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/SymbolVersionRequirement.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/SymbolVersionAuxRequirement.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/Binary.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/Symbol.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/EnumToString.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/Header.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/DynamicSharedObject.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/Segment.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/Section.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/SymbolVersionAux.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/GnuHash.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/SysvHash.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/SymbolVersion.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/Builder.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/DynamicEntryLibrary.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/DataHandler/Node.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/DataHandler/Handler.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/Parser.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/Relocation.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/DynamicEntryRunPath.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/SymbolVersionDefinition.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/DynamicEntry.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/DynamicEntryRpath.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/DynamicEntryFlags.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/Builder.tcc"
        "${CMAKE_CURRENT_LIST_DIR}/Parser.tcc"
        "${CMAKE_CURRENT_LIST_DIR}/Binary.tcc"
        "${CMAKE_CURRENT_LIST_DIR}/hash.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/Note.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/NoteDetails.cpp"
        )

set(LIEF_ELF_INC_FILES
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/Binary.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/Builder.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/DynamicEntry.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/DynamicEntryArray.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/DynamicEntryFlags.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/DynamicEntryLibrary.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/DynamicEntryRpath.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/DynamicEntryRunPath.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/DynamicSharedObject.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/EnumToString.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/GnuHash.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/SysvHash.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/Header.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/Parser.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/Relocation.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/Section.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/Segment.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/Symbol.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/SymbolVersion.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/SymbolVersionAux.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/SymbolVersionAuxRequirement.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/SymbolVersionDefinition.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/SymbolVersionRequirement.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/type_traits.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/utils.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/hash.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/Note.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/NoteDetails.hpp"

        "${CMAKE_CURRENT_BINARY_DIR}/include/LIEF/ELF/Structures.hpp"
        "${CMAKE_CURRENT_BINARY_DIR}/include/LIEF/ELF/enums.hpp"

        "${CMAKE_CURRENT_LIST_DIR}/RelocationSizes.hpp"
        )

set(LIEF_ELF_DATA_HANDLER_INCLUDE_FILES
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/DataHandler/Handler.hpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/DataHandler/Node.hpp")

# JSON Part
# =========
set(LIEF_ELF_JSON_SRC "${CMAKE_CURRENT_LIST_DIR}/json.cpp")
set(LIEF_ELF_JSON_HDR "${CMAKE_CURRENT_SOURCE_DIR}/include/LIEF/ELF/json.hpp")

# Note Details Part
# =================
include("${CMAKE_CURRENT_LIST_DIR}/NoteDetails/CMakeLists.txt")

if (LIEF_ENABLE_JSON)
        list(APPEND LIEF_ELF_SRC       ${LIEF_ELF_JSON_SRC})
        list(APPEND LIEF_ELF_INC_FILES ${LIEF_ELF_JSON_HDR})
endif()

set(LIEF_ELF_INCLUDE_FILES
        ${LIEF_ELF_INC_FILES}
        #  ${LIEF_ELF_RELOC_INCLUDE_FILES}
        ${LIEF_ELF_DATA_HANDLER_INCLUDE_FILES})

source_group("Source Files\\ELF" FILES ${LIEF_ELF_SRC})
source_group("Header Files\\ELF" FILES ${LIEF_ELF_INC_FILES})
source_group("Header Files\\ELF\\DataHandler" FILES ${LIEF_ELF_DATA_HANDLER_INCLUDE_FILES})
#source_group("Header Files\\ELF\\Relocations" FILES ${LIEF_ELF_RELOC_INCLUDE_FILES})

if (LIEF_ELF)
        target_sources(LIB_LIEF PRIVATE
                ${LIEF_ELF_SRC}
                ${LIEF_ELF_INCLUDE_FILES})
        target_compile_options(LIB_LIEF PRIVATE -g -O0)
endif()
