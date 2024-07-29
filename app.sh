#!/bin/bash

#环境变量设置
export otel.traces.exporter=none
export otel.metrics.exporter=none

# 定义Spring Boot应用的名称
APP_NAME="fabric-0.0.1-SNAPSHOT"

# 定义Spring Boot应用的JAR文件
APP_JAR="$APP_NAME.jar"

# 定义Spring Boot应用的PID文件
PID_FILE="application.pid"

# 定义启动Spring Boot应用的命令
start() {
    echo "Starting $APP_NAME..."
    nohup java -jar $APP_JAR > /dev/null 2>&1 &
    echo $! > $PID_FILE
    echo "$APP_NAME started successfully."
}

# 定义停止Spring Boot应用的命令
stop() {
    echo "Stopping $APP_NAME..."
    kill `cat $PID_FILE`
    rm -f $PID_FILE
    echo "$APP_NAME stopped successfully."
}

# 根据传入的参数调用相应的函数
case "$1" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    *)
        echo "Usage: $0 {start|stop}"
        exit 1
esac

exit 0