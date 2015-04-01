#!/usr/bin/env ruby

# Builds, installs, and runs the Skeleton activity from the command line

require 'js_base'

PACKAGE = "com.js.askeleton"

puts("Building...")
scall("adb shell am force-stop #{PACKAGE}")
scall("gradle installDebug")
puts("Starting...")
scall("adb shell am start -n #{PACKAGE}/.SkeletonActivity")
