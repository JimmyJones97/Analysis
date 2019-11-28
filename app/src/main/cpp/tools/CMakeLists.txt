project(tools)
file(GLOB SRC_UTILS ${PROJECT_SOURCE_DIR}/*.cpp)
include_directories(${PROJECT_SOURCE_DIR})
add_library(${PROJECT_NAME} STATIC ${SRC_UTILS})
target_include_directories(${PROJECT_NAME}
        INTERFACE
        $<BUILD_INTERFACE:${PROJECT_SOURCE_DIR}/>
        $<INSTALL_INTERFACE:include>
        )
target_compile_options(${PROJECT_NAME}
        BEFORE
        PUBLIC -DLOG_TAG="TOOLS")
message(STATUS "当前目录:${CMAKE_CURRENT_SOURCE_DIR}")
