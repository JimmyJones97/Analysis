project(HookUtil C )

include_directories(${CMAKE_CURRENT_SOURCE_DIR})
add_library(${PROJECT_NAME} SHARED    hook_main.cpp   )
target_link_libraries(${PROJECT_NAME}  log   )
#target_compile_options()
#target_link_libraries()
#target_link_options()
#target_compile_definitions()
#target_include_directories()
#target_compile_features()
#target_link_directories()
#target_sources()
#add_custom_target()
#get_target_property()
#set_target_properties()



