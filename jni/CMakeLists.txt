cmake_minimum_required(VERSION 3.4.1)
project(audio-effect)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -O2 -Wall -std=c++11 -fexceptions -fpermissive -fpic")

include_directories(include)
link_directories(${PROJECT_SOURCE_DIR}/libs/${ANDROID_ABI}/)
link_directories($ENV{EXTRA_LIB_DIR})

set(SOURCE_FILES
        src/audio_effect_engine_native.cpp)

add_library(${PROJECT_NAME} SHARED ${SOURCE_FILES})

# Searches for a specified prebuilt library and stores the path as a
# variable. Because system libraries are included in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in the
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        ${PROJECT_NAME}

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})