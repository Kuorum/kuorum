import net.spy.memcached.AddrUtil
import net.spy.memcached.ConnectionFactoryBuilder
import net.spy.memcached.MemcachedClient
import net.spy.memcached.transcoders.WhalinTranscoder
import spock.lang.Ignore
import spock.lang.Specification

class SpyMemcacheConnection extends Specification {

    //To check connection and function on local memcached
    @Ignore
    void 'Connecting'() {
        when:
//        MemcachedClient cache = new MemcachedClient(new InetSocketAddress("127.0.0.1",11211))
        MemcachedClient cache = new MemcachedClient(new ConnectionFactoryBuilder()
                .setTranscoder(new WhalinTranscoder())
                .setProtocol(ConnectionFactoryBuilder.Protocol.BINARY)
                .build(), AddrUtil.getAddresses("127.0.0.1:11211"))

//        def content = new ResponseContent()
        def operationFuture = cache.set("key", 0, "content")
        def cached = cache.get("key")
        def servers = cache.getAvailableServers()
        then:
        operationFuture.getStatus().isSuccess()
        cached as String == "content"
        servers.size() == 1
    }
}
