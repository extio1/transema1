set(TARGET_NAME TransemaLLVMCodegen)

file(GLOB HEADERS ${CMAKE_CURRENT_SOURCE_DIR}/*.h)
file(GLOB SOURCES ${CMAKE_CURRENT_SOURCE_DIR}/*.cpp)

set(LLVM_LIBS LLVMSupport)

add_library(${TARGET_NAME} SHARED ${SOURCES} ${HEADERS})
target_include_directories(${TARGET_NAME} PRIVATE ${TRANSEMA_INCLUDE_DIR})
target_link_libraries(${TARGET_NAME} ${LLVM_LIBS})
