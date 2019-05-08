project(slua)

file(GLOB SRC_LUA ${PROJECT_SOURCE_DIR}/lua/*.cpp)
file(GLOB SRC_LUASOCKET ${PROJECT_SOURCE_DIR}/luasocket/*.cpp)

include_directories(
        ${PROJECT_SOURCE_DIR}/lua
        ${PROJECT_SOURCE_DIR}/luasocket
)

add_library(${PROJECT_NAME} STATIC ${SRC_LUA} ${SRC_LUASOCKET}) #静态slua

target_include_directories(${PROJECT_NAME}
        INTERFACE
        $<BUILD_INTERFACE:${CMAKE_CURRENT_SOURCE_DIR}/lua>
        $<INSTALL_INTERFACE:include>
        )

option(USE_SLUA "使用slua_mod模块来进行开发 " ON)
if(USE_SLUA)

        message(STATUS " USE_SLUA${tess}")
#    include(CheckSymbolExists)
#    set(CMAKE_REQUIRED_LIBRARIES "m")
#    check_symbol_exists(log "math.h" HAVE_LOG)
#    check_symbol_exists(exp "math.h" HAVE_EXP)
#    add_custom_command(
#            OUTPUT ${CMAKE_CURRENT_BINARY_DIR}/stdout.out
#            COMMAND dir
#            DEPENDS dir
#    )
endif()