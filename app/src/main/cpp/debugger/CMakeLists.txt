project(debugger)


add_executable(${PROJECT_NAME} ${CMAKE_CURRENT_SOURCE_DIR}/hoster/debugger.cpp)
target_link_libraries(${PROJECT_NAME} linenoise)
add_executable(${PROJECT_NAME} ${CMAKE_CURRENT_SOURCE_DIR}/stuber/stuber.cpp)
