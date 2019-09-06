project(debugger)


add_executable(${PROJECT_NAME} ${CMAKE_CURRENT_SOURCE_DIR}/hoster/debugger.cpp)
add_executable(test_cli ${CMAKE_CURRENT_SOURCE_DIR}/hoster/test_cli.cpp)
target_link_libraries(${PROJECT_NAME} linenoise)

add_executable(stuber ${CMAKE_CURRENT_SOURCE_DIR}/stuber/stuber.cpp ${CMAKE_CURRENT_SOURCE_DIR}/stuber/krnel.cpp)
target_include_directories(stuber PRIVATE ${CMAKE_CURRENT_SOURCE_DIR}/stuber/)
target_link_libraries(stuber Utils)