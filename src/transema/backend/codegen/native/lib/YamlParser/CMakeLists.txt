set(TARGET_NAME TransemaYamlParser)

file(GLOB HEADERS ${CMAKE_CURRENT_SOURCE_DIR}/*.h)
file(GLOB SOURCES ${CMAKE_CURRENT_SOURCE_DIR}/*.cpp)

add_library(${TARGET_NAME} SHARED ${SOURCES} ${HEADERS})
target_include_directories(${TARGET_NAME} PRIVATE ${TRANSEMA_INCLUDE_DIR})
target_link_libraries(${TARGET_NAME} 
    PUBLIC
        LLVMSupport
)
