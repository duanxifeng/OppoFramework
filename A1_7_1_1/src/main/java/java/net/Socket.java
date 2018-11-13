package java.net;

import dalvik.system.VMRuntime;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.Proxy.Type;
import java.nio.channels.SocketChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import libcore.io.IoBridge;
import sun.net.ApplicationProxy;
import sun.security.util.SecurityConstants;

/*  JADX ERROR: NullPointerException in pass: ReSugarCode
    java.lang.NullPointerException
    	at jadx.core.dex.visitors.ReSugarCode.initClsEnumMap(ReSugarCode.java:159)
    	at jadx.core.dex.visitors.ReSugarCode.visit(ReSugarCode.java:44)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:12)
    	at jadx.core.ProcessClass.process(ProcessClass.java:32)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
    	at java.lang.Iterable.forEach(Iterable.java:75)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
    	at jadx.core.ProcessClass.process(ProcessClass.java:37)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
    */
/*  JADX ERROR: NullPointerException in pass: ExtractFieldInit
    java.lang.NullPointerException
    	at jadx.core.dex.visitors.ExtractFieldInit.checkStaticFieldsInit(ExtractFieldInit.java:58)
    	at jadx.core.dex.visitors.ExtractFieldInit.visit(ExtractFieldInit.java:44)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:12)
    	at jadx.core.ProcessClass.process(ProcessClass.java:32)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
    	at java.lang.Iterable.forEach(Iterable.java:75)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
    	at jadx.core.ProcessClass.process(ProcessClass.java:37)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
    */
public class Socket implements Closeable {
    private static Method enforceCheckPermissionMethod;
    private static SocketImplFactory factory;
    private static long sIndex;
    private boolean bound;
    private Object closeLock;
    private boolean closed;
    private boolean connected;
    private boolean created;
    SocketImpl impl;
    private InetAddress localAddress;
    private boolean oldImpl;
    private boolean shutIn;
    private boolean shutOut;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: bogus opcode: 0073 in method: java.net.Socket.<clinit>():void, dex: 
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:118)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:248)
        	at jadx.core.ProcessClass.process(ProcessClass.java:29)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.lang.Iterable.forEach(Iterable.java:75)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        Caused by: java.lang.IllegalArgumentException: bogus opcode: 0073
        	at com.android.dx.io.OpcodeInfo.get(OpcodeInfo.java:1227)
        	at com.android.dx.io.OpcodeInfo.getName(OpcodeInfo.java:1234)
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:581)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:74)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:104)
        	... 9 more
        */
    static {
        /*
        // Can't load method instructions: Load method exception: bogus opcode: 0073 in method: java.net.Socket.<clinit>():void, dex: 
        */
        throw new UnsupportedOperationException("Method not decompiled: java.net.Socket.<clinit>():void");
    }

    public Socket() {
        this.created = false;
        this.bound = false;
        this.connected = false;
        this.closed = false;
        this.closeLock = new Object();
        this.shutIn = false;
        this.shutOut = false;
        this.oldImpl = false;
        this.localAddress = Inet6Address.ANY;
        setImpl();
    }

    public Socket(Proxy proxy) {
        this.created = false;
        this.bound = false;
        this.connected = false;
        this.closed = false;
        this.closeLock = new Object();
        this.shutIn = false;
        this.shutOut = false;
        this.oldImpl = false;
        this.localAddress = Inet6Address.ANY;
        if (proxy == null) {
            throw new IllegalArgumentException("Invalid Proxy");
        }
        Proxy p = proxy == Proxy.NO_PROXY ? Proxy.NO_PROXY : ApplicationProxy.create(proxy);
        if (p.type() == Type.SOCKS) {
            SecurityManager security = System.getSecurityManager();
            InetSocketAddress epoint = (InetSocketAddress) p.address();
            if (epoint.getAddress() != null) {
                checkAddress(epoint.getAddress(), "Socket");
            }
            if (security != null) {
                if (epoint.isUnresolved()) {
                    epoint = new InetSocketAddress(epoint.getHostName(), epoint.getPort());
                }
                if (epoint.isUnresolved()) {
                    security.checkConnect(epoint.getHostName(), epoint.getPort());
                } else {
                    security.checkConnect(epoint.getAddress().getHostAddress(), epoint.getPort());
                }
            }
            this.impl = new SocksSocketImpl(p);
            this.impl.setSocket(this);
        } else if (p != Proxy.NO_PROXY) {
            throw new IllegalArgumentException("Invalid Proxy");
        } else if (factory == null) {
            this.impl = new PlainSocketImpl();
            this.impl.setSocket(this);
        } else {
            setImpl();
        }
    }

    protected Socket(SocketImpl impl) throws SocketException {
        this.created = false;
        this.bound = false;
        this.connected = false;
        this.closed = false;
        this.closeLock = new Object();
        this.shutIn = false;
        this.shutOut = false;
        this.oldImpl = false;
        this.localAddress = Inet6Address.ANY;
        this.impl = impl;
        if (impl != null) {
            checkOldImpl();
            this.impl.setSocket(this);
        }
    }

    public Socket(String host, int port) throws UnknownHostException, IOException {
        this(InetAddress.getAllByName(host), port, (SocketAddress) null, true);
    }

    public Socket(InetAddress address, int port) throws IOException {
        this(nonNullAddress(address), port, (SocketAddress) null, true);
    }

    public Socket(String host, int port, InetAddress localAddr, int localPort) throws IOException {
        this(InetAddress.getAllByName(host), port, new InetSocketAddress(localAddr, localPort), true);
    }

    public Socket(InetAddress address, int port, InetAddress localAddr, int localPort) throws IOException {
        this(nonNullAddress(address), port, new InetSocketAddress(localAddr, localPort), true);
    }

    @Deprecated
    public Socket(String host, int port, boolean stream) throws IOException {
        this(InetAddress.getAllByName(host), port, (SocketAddress) null, stream);
    }

    @Deprecated
    public Socket(InetAddress host, int port, boolean stream) throws IOException {
        this(nonNullAddress(host), port, new InetSocketAddress(0), stream);
    }

    private static InetAddress[] nonNullAddress(InetAddress address) {
        if (address == null) {
            throw new NullPointerException();
        }
        InetAddress[] inetAddressArr = new InetAddress[1];
        inetAddressArr[0] = address;
        return inetAddressArr;
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0046 A:{ExcHandler: java.io.IOException (r2_0 'e' java.lang.Exception), Splitter: B:10:0x0033} */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0046 A:{ExcHandler: java.io.IOException (r2_0 'e' java.lang.Exception), Splitter: B:10:0x0033} */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x005a A:{LOOP_END, LOOP:0: B:7:0x002d->B:23:0x005a} */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0054 A:{SYNTHETIC} */
    /* JADX WARNING: Missing block: B:15:0x0046, code:
            r2 = move-exception;
     */
    /* JADX WARNING: Missing block: B:17:?, code:
            r7.impl.close();
            r7.closed = true;
     */
    /* JADX WARNING: Missing block: B:20:0x0054, code:
            throw r2;
     */
    /* JADX WARNING: Missing block: B:21:0x0055, code:
            r1 = move-exception;
     */
    /* JADX WARNING: Missing block: B:22:0x0056, code:
            r2.addSuppressed(r1);
     */
    /* JADX WARNING: Missing block: B:23:0x005a, code:
            r7.impl = null;
            r7.created = false;
            r7.bound = false;
            r7.closed = false;
            r3 = r3 + 1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private Socket(InetAddress[] addresses, int port, SocketAddress localAddr, boolean stream) throws IOException {
        int i;
        this.created = false;
        this.bound = false;
        this.connected = false;
        this.closed = false;
        this.closeLock = new Object();
        this.shutIn = false;
        this.shutOut = false;
        this.oldImpl = false;
        this.localAddress = Inet6Address.ANY;
        if (addresses == null || addresses.length == 0) {
            throw new SocketException("Impossible: empty address list");
        }
        i = 0;
        while (i < addresses.length) {
            setImpl();
            try {
                InetSocketAddress address = new InetSocketAddress(addresses[i], port);
                createImpl(stream);
                if (localAddr != null) {
                    bind(localAddr);
                }
                connect(address);
                return;
            } catch (Exception e) {
            }
        }
        return;
        if (i != addresses.length - 1) {
        }
    }

    void createImpl(boolean stream) throws SocketException {
        if (this.impl == null) {
            setImpl();
        }
        try {
            this.impl.create(stream);
            this.created = true;
        } catch (IOException e) {
            throw new SocketException(e.getMessage());
        }
    }

    private void checkOldImpl() {
        if (this.impl != null) {
            this.oldImpl = ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
                public Boolean run() {
                    Class[] cl = new Class[2];
                    cl[0] = SocketAddress.class;
                    cl[1] = Integer.TYPE;
                    Class clazz = Socket.this.impl.getClass();
                    do {
                        try {
                            clazz.getDeclaredMethod(SecurityConstants.SOCKET_CONNECT_ACTION, cl);
                            return Boolean.FALSE;
                        } catch (NoSuchMethodException e) {
                            clazz = clazz.getSuperclass();
                            if (clazz.equals(SocketImpl.class)) {
                                return Boolean.TRUE;
                            }
                        }
                    } while (clazz.equals(SocketImpl.class));
                    return Boolean.TRUE;
                }
            })).booleanValue();
        }
    }

    void setImpl() {
        if (factory != null) {
            this.impl = factory.createSocketImpl();
            checkOldImpl();
        } else {
            this.impl = new SocksSocketImpl();
        }
        if (this.impl != null) {
            this.impl.setSocket(this);
        }
    }

    SocketImpl getImpl() throws SocketException {
        if (!this.created) {
            createImpl(true);
        }
        return this.impl;
    }

    public void connect(SocketAddress endpoint) throws IOException {
        connect(endpoint, 0);
    }

    public void connect(SocketAddress endpoint, int timeout) throws IOException {
        if (endpoint == null) {
            throw new IllegalArgumentException("connect: The address can't be null");
        } else if (timeout < 0) {
            throw new IllegalArgumentException("connect: timeout can't be negative");
        } else if (isClosed()) {
            throw new SocketException("Socket is closed");
        } else if (!this.oldImpl && isConnected()) {
            throw new SocketException("already connected");
        } else if (endpoint instanceof InetSocketAddress) {
            SocketAddress epoint = (InetSocketAddress) endpoint;
            InetAddress addr = epoint.getAddress();
            int port = epoint.getPort();
            checkAddress(addr, SecurityConstants.SOCKET_CONNECT_ACTION);
            if (!isEmailSendPort(port) || checkEmailSendRequest()) {
                SecurityManager security = System.getSecurityManager();
                if (security != null) {
                    if (epoint.isUnresolved()) {
                        security.checkConnect(epoint.getHostName(), port);
                    } else {
                        security.checkConnect(addr.getHostAddress(), port);
                    }
                }
                String[] vmProperties = VMRuntime.getRuntime().properties();
                boolean debug = false;
                for (int i = 0; i < vmProperties.length; i++) {
                    int split = vmProperties[i].indexOf(61);
                    String key = vmProperties[i].substring(0, split);
                    String val = vmProperties[i].substring(split + 1);
                    if (key.equals("build-type") && ("eng".equals(val) || "userdebug".equals(val))) {
                        if ("eng".equals(val)) {
                            System.out.println("[socket][" + sIndex + "] connection " + endpoint + ";LocalPort=" + getLocalPort() + "(" + timeout + ")");
                        }
                        debug = true;
                    }
                }
                sIndex++;
                try {
                    if (!this.created) {
                        createImpl(true);
                    }
                    if (!this.oldImpl) {
                        this.impl.connect(epoint, timeout);
                    } else if (timeout != 0) {
                        throw new UnsupportedOperationException("SocketImpl.connect(addr, timeout)");
                    } else if (epoint.isUnresolved()) {
                        this.impl.connect(addr.getHostName(), port);
                    } else {
                        this.impl.connect(addr, port);
                    }
                    cacheLocalAddress();
                    this.connected = true;
                    if (debug) {
                        System.out.println("[socket][" + this.localAddress + ":" + getLocalPort() + "] connected");
                    }
                    this.bound = true;
                    return;
                } catch (IOException ioe) {
                    System.out.println("[socket][" + sIndex + ":" + getLocalPort() + "] exception");
                    throw ioe;
                }
            }
            System.out.println("Fail to send due to mom user permission");
            throw new UnknownHostException("User dendied by MoM");
        } else {
            throw new IllegalArgumentException("Unsupported address type");
        }
    }

    public void bind(SocketAddress bindpoint) throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        } else if (!this.oldImpl && isBound()) {
            throw new SocketException("Already bound");
        } else if (bindpoint == null || (bindpoint instanceof InetSocketAddress)) {
            InetSocketAddress epoint = (InetSocketAddress) bindpoint;
            if (epoint == null || !epoint.isUnresolved()) {
                if (epoint == null) {
                    epoint = new InetSocketAddress(0);
                }
                InetAddress addr = epoint.getAddress();
                int port = epoint.getPort();
                checkAddress(addr, "bind");
                getImpl().bind(addr, port);
                cacheLocalAddress();
                this.bound = true;
                return;
            }
            throw new SocketException("Unresolved address");
        } else {
            throw new IllegalArgumentException("Unsupported address type");
        }
    }

    private void checkAddress(InetAddress addr, String op) {
        if (addr != null) {
            if (!(!(addr instanceof Inet4Address) ? addr instanceof Inet6Address : true)) {
                throw new IllegalArgumentException(op + ": invalid address type");
            }
        }
    }

    private void cacheLocalAddress() throws SocketException {
        this.localAddress = IoBridge.getSocketLocalAddress(this.impl.fd);
    }

    final void postAccept() {
        this.connected = true;
        this.created = true;
        this.bound = true;
    }

    void setCreated() {
        this.created = true;
    }

    void setBound() {
        this.bound = true;
    }

    void setConnected() {
        this.connected = true;
    }

    public InetAddress getInetAddress() {
        if (!isConnected()) {
            return null;
        }
        try {
            return getImpl().getInetAddress();
        } catch (SocketException e) {
            return null;
        }
    }

    public InetAddress getLocalAddress() {
        if (!isBound()) {
            return InetAddress.anyLocalAddress();
        }
        InetAddress in;
        try {
            in = (InetAddress) getImpl().getOption(15);
            if (!NetUtil.doRevealLocalAddress()) {
                SecurityManager sm = System.getSecurityManager();
                if (sm != null) {
                    sm.checkConnect(in.getHostAddress(), -1);
                }
            }
            if (in.isAnyLocalAddress()) {
                in = InetAddress.anyLocalAddress();
            }
        } catch (SecurityException e) {
            in = InetAddress.getLoopbackAddress();
        } catch (Exception e2) {
            in = InetAddress.anyLocalAddress();
        }
        return in;
    }

    public int getPort() {
        if (!isConnected()) {
            return 0;
        }
        try {
            return getImpl().getPort();
        } catch (SocketException e) {
            return -1;
        }
    }

    public int getLocalPort() {
        if (!isBound()) {
            return -1;
        }
        try {
            return getImpl().getLocalPort();
        } catch (SocketException e) {
            return -1;
        }
    }

    public SocketAddress getRemoteSocketAddress() {
        if (isConnected()) {
            return new InetSocketAddress(getInetAddress(), getPort());
        }
        return null;
    }

    public SocketAddress getLocalSocketAddress() {
        if (isBound()) {
            return new InetSocketAddress(getLocalAddress(), getLocalPort());
        }
        return null;
    }

    public SocketChannel getChannel() {
        return null;
    }

    public InputStream getInputStream() throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        } else if (!isConnected()) {
            throw new SocketException("Socket is not connected");
        } else if (isInputShutdown()) {
            throw new SocketException("Socket input is shutdown");
        } else {
            try {
                return (InputStream) AccessController.doPrivileged(new PrivilegedExceptionAction<InputStream>() {
                    public InputStream run() throws IOException {
                        return Socket.this.impl.getInputStream();
                    }
                });
            } catch (PrivilegedActionException e) {
                throw ((IOException) e.getException());
            }
        }
    }

    public OutputStream getOutputStream() throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        } else if (!isConnected()) {
            throw new SocketException("Socket is not connected");
        } else if (isOutputShutdown()) {
            throw new SocketException("Socket output is shutdown");
        } else {
            try {
                return (OutputStream) AccessController.doPrivileged(new PrivilegedExceptionAction<OutputStream>() {
                    public OutputStream run() throws IOException {
                        return Socket.this.impl.getOutputStream();
                    }
                });
            } catch (PrivilegedActionException e) {
                throw ((IOException) e.getException());
            }
        }
    }

    public void setTcpNoDelay(boolean on) throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        getImpl().setOption(1, Boolean.valueOf(on));
    }

    public boolean getTcpNoDelay() throws SocketException {
        if (!isClosed()) {
            return ((Boolean) getImpl().getOption(1)).booleanValue();
        }
        throw new SocketException("Socket is closed");
    }

    public void setSoLinger(boolean on, int linger) throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        } else if (!on) {
            getImpl().setOption(128, new Boolean(on));
        } else if (linger < 0) {
            throw new IllegalArgumentException("invalid value for SO_LINGER");
        } else {
            if (linger > 65535) {
                linger = 65535;
            }
            getImpl().setOption(128, new Integer(linger));
        }
    }

    public int getSoLinger() throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        Object o = getImpl().getOption(128);
        if (o instanceof Integer) {
            return ((Integer) o).intValue();
        }
        return -1;
    }

    public void sendUrgentData(int data) throws IOException {
        if (getImpl().supportsUrgentData()) {
            getImpl().sendUrgentData(data);
            return;
        }
        throw new SocketException("Urgent data not supported");
    }

    public void setOOBInline(boolean on) throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        getImpl().setOption(SocketOptions.SO_OOBINLINE, Boolean.valueOf(on));
    }

    public boolean getOOBInline() throws SocketException {
        if (!isClosed()) {
            return ((Boolean) getImpl().getOption(SocketOptions.SO_OOBINLINE)).booleanValue();
        }
        throw new SocketException("Socket is closed");
    }

    public synchronized void setSoTimeout(int timeout) throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        } else if (timeout < 0) {
            throw new IllegalArgumentException("timeout can't be negative");
        } else {
            getImpl().setOption(SocketOptions.SO_TIMEOUT, new Integer(timeout));
        }
    }

    public synchronized int getSoTimeout() throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        Object o = getImpl().getOption(SocketOptions.SO_TIMEOUT);
        if (!(o instanceof Integer)) {
            return 0;
        }
        return ((Integer) o).intValue();
    }

    public synchronized void setSoSndTimeout(int timeout) throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        } else if (timeout < 0) {
            throw new IllegalArgumentException("timeout can't be negative");
        } else {
            getImpl().setOption(SocketOptions.SO_SND_TIMEOUT, new Integer(timeout));
        }
    }

    public synchronized int getSoSndTimeout() throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        Object o = getImpl().getOption(SocketOptions.SO_SND_TIMEOUT);
        if (!(o instanceof Integer)) {
            return 0;
        }
        return ((Integer) o).intValue();
    }

    public synchronized void setSendBufferSize(int size) throws SocketException {
        if (size <= 0) {
            throw new IllegalArgumentException("negative send size");
        } else if (isClosed()) {
            throw new SocketException("Socket is closed");
        } else {
            getImpl().setOption(SocketOptions.SO_SNDBUF, new Integer(size));
        }
    }

    public synchronized int getSendBufferSize() throws SocketException {
        int result;
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        result = 0;
        Object o = getImpl().getOption(SocketOptions.SO_SNDBUF);
        if (o instanceof Integer) {
            result = ((Integer) o).intValue();
        }
        return result;
    }

    public synchronized void setReceiveBufferSize(int size) throws SocketException {
        if (size <= 0) {
            throw new IllegalArgumentException("invalid receive size");
        } else if (isClosed()) {
            throw new SocketException("Socket is closed");
        } else {
            getImpl().setOption(SocketOptions.SO_RCVBUF, new Integer(size));
        }
    }

    public synchronized int getReceiveBufferSize() throws SocketException {
        int result;
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        result = 0;
        Object o = getImpl().getOption(SocketOptions.SO_RCVBUF);
        if (o instanceof Integer) {
            result = ((Integer) o).intValue();
        }
        return result;
    }

    public void setKeepAlive(boolean on) throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        getImpl().setOption(8, Boolean.valueOf(on));
    }

    public boolean getKeepAlive() throws SocketException {
        if (!isClosed()) {
            return ((Boolean) getImpl().getOption(8)).booleanValue();
        }
        throw new SocketException("Socket is closed");
    }

    public void setTrafficClass(int tc) throws SocketException {
        if (tc < 0 || tc > 255) {
            throw new IllegalArgumentException("tc is not in range 0 -- 255");
        } else if (isClosed()) {
            throw new SocketException("Socket is closed");
        } else {
            getImpl().setOption(3, new Integer(tc));
        }
    }

    public int getTrafficClass() throws SocketException {
        return ((Integer) getImpl().getOption(3)).intValue();
    }

    public void setReuseAddress(boolean on) throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        getImpl().setOption(4, Boolean.valueOf(on));
    }

    public boolean getReuseAddress() throws SocketException {
        if (!isClosed()) {
            return ((Boolean) getImpl().getOption(4)).booleanValue();
        }
        throw new SocketException("Socket is closed");
    }

    public synchronized void close() throws IOException {
        synchronized (this.closeLock) {
            if (isClosed()) {
                return;
            }
            if (this.created) {
                String[] vmProperties = VMRuntime.getRuntime().properties();
                for (int i = 0; i < vmProperties.length; i++) {
                    int split = vmProperties[i].indexOf(61);
                    String key = vmProperties[i].substring(0, split);
                    String val = vmProperties[i].substring(split + 1);
                    if (key.equals("build-type") && ("eng".equals(val) || "userdebug".equals(val))) {
                        System.out.println("close [socket][" + this.localAddress + ":" + getLocalPort() + "]");
                    }
                }
                this.localAddress = Inet6Address.ANY;
                this.impl.close();
            }
            this.closed = true;
        }
    }

    public void shutdownInput() throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        } else if (!isConnected()) {
            throw new SocketException("Socket is not connected");
        } else if (isInputShutdown()) {
            throw new SocketException("Socket input is already shutdown");
        } else {
            getImpl().shutdownInput();
            this.shutIn = true;
        }
    }

    public void shutdownOutput() throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        } else if (!isConnected()) {
            throw new SocketException("Socket is not connected");
        } else if (isOutputShutdown()) {
            throw new SocketException("Socket output is already shutdown");
        } else {
            getImpl().shutdownOutput();
            this.shutOut = true;
        }
    }

    public String toString() {
        try {
            if (isConnected()) {
                return "Socket[address=" + getImpl().getInetAddress() + ",port=" + getImpl().getPort() + ",localPort=" + getImpl().getLocalPort() + "]";
            }
        } catch (SocketException e) {
        }
        return "Socket[unconnected]";
    }

    public boolean isConnected() {
        return !this.connected ? this.oldImpl : true;
    }

    public boolean isBound() {
        return !this.bound ? this.oldImpl : true;
    }

    public boolean isClosed() {
        boolean z;
        synchronized (this.closeLock) {
            z = this.closed;
        }
        return z;
    }

    public boolean isInputShutdown() {
        return this.shutIn;
    }

    public boolean isOutputShutdown() {
        return this.shutOut;
    }

    public static synchronized void setSocketImplFactory(SocketImplFactory fac) throws IOException {
        synchronized (Socket.class) {
            if (factory != null) {
                throw new SocketException("factory already defined");
            }
            SecurityManager security = System.getSecurityManager();
            if (security != null) {
                security.checkSetFactory();
            }
            factory = fac;
        }
    }

    public void setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
    }

    public FileDescriptor getFileDescriptor$() {
        return this.impl.getFileDescriptor();
    }

    private boolean enforceCheckPermission(String permission, String action) {
        try {
            Method method;
            synchronized (Socket.class) {
                if (enforceCheckPermissionMethod == null) {
                    Class[] clsArr = new Class[2];
                    clsArr[0] = String.class;
                    clsArr[1] = String.class;
                    enforceCheckPermissionMethod = Class.forName("com.mediatek.cta.CtaUtils").getMethod("enforceCheckPermission", clsArr);
                }
                method = enforceCheckPermissionMethod;
            }
            Object[] objArr = new Object[2];
            objArr[0] = permission;
            objArr[1] = action;
            return ((Boolean) method.invoke(null, objArr)).booleanValue();
        } catch (ReflectiveOperationException e) {
            if (!(e.getCause() instanceof SecurityException)) {
                return true;
            }
            throw new SecurityException(e.getCause());
        }
    }

    private boolean checkEmailSendRequest() {
        return enforceCheckPermission("com.mediatek.permission.CTA_SEND_EMAIL", "Send emails");
    }

    private boolean isEmailSendPort(int port) {
        return port == 25 || port == 465 || port == 587;
    }
}