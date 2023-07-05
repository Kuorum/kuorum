
export MEMCACHED_END_POINT=kuorum-dev-cache.0ok9f8.cfg.euw1.cache.amazonaws.com
export MEMCACHED_PORT=11211


export JAVA_OPTS="-Dfile.encoding=UTF-8
  -Xms128m -Xmx2048m
  -XX:PermSize=64m
  -XX:MaxPermSize=2048m
  -Djava.security.egd=file:/dev/./urandom
  -DMEMCACHED_END_POINT=${MEMCACHED_END_POINT}
  -DMEMCACHED_PORT=${MEMCACHED_PORT}
  "
