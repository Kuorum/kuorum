
export MEMCACHED_END_POINT=__MEMCACHED_END_POINT__
export MEMCACHED_PORT=__MEMCACHED_PORT__


export JAVA_OPTS="-Dfile.encoding=UTF-8
  -Xms128m -Xmx2048m
  -XX:PermSize=64m
  -XX:MaxPermSize=2048m
  -Djava.security.egd=file:/dev/./urandom
  -DMEMCACHED_END_POINT=${MEMCACHED_END_POINT}
  -DMEMCACHED_PORT=${MEMCACHED_PORT}
  "
