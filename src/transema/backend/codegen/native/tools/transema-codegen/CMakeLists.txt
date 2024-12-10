set(TARGET_NAME transema-codegen)

file(GLOB_RECURSE HEADERS ${CMAKE_CURRENT_SOURCE_DIR}/*.h)
file(GLOB_RECURSE SOURCES ${CMAKE_CURRENT_SOURCE_DIR}/*.cpp)

set(LLVM_LIBS LLVMSupport LLVMIRReader LLVMBitWriter)

add_executable(${TARGET_NAME} ${SOURCES} ${HEADERS})
target_include_directories(${TARGET_NAME} PRIVATE ${TRANSEMA_INCLUDE_DIR})
target_link_libraries(${TARGET_NAME}
    PUBLIC
        ${LLVM_LIBS}
        TransemaLLVMCodegen
        TransemaYamlParser  
)
