project(inject  CXX)


include_directories(${CMAKE_CURRENT_SOURCE_DIR})
add_executable(${PROJECT_NAME} inject.cpp inject_main.cpp)
target_link_libraries(${PROJECT_NAME} log)

