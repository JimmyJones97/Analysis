project(HookUtil C )

include_directories(${CMAKE_CURRENT_SOURCE_DIR})
add_library(${PROJECT_NAME} SHARED    hook_main.cpp   )
target_link_libraries(${PROJECT_NAME}  log   )




