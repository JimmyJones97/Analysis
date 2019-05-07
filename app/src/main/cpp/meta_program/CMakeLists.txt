cmake_minimum_required(VERSION 3.4.1)
project(meta)

include_directories(${PROJECT_SOURCE_DIR}/)
add_executable(powe ${PROJECT_SOURCE_DIR}/main.cpp)

#check attributes
message(STATUS "构建版本:${CMAKE_BUILD_TYPE}")
message(STATUS "源码:${SRC_LIST}\n ,${CMAKE_CXX_FLAGS} ")
