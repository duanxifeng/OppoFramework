package android.os;

/*  JADX ERROR: NullPointerException in pass: ExtractFieldInit
    java.lang.NullPointerException
    	at jadx.core.utils.BlockUtils.isAllBlocksEmpty(BlockUtils.java:546)
    	at jadx.core.dex.visitors.ExtractFieldInit.getConstructorsList(ExtractFieldInit.java:221)
    	at jadx.core.dex.visitors.ExtractFieldInit.moveCommonFieldsInit(ExtractFieldInit.java:121)
    	at jadx.core.dex.visitors.ExtractFieldInit.visit(ExtractFieldInit.java:46)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:12)
    	at jadx.core.ProcessClass.process(ProcessClass.java:32)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
    */
public class OppoBatteryManager {
    public static final String ACTION_ADDITIONAL_BATTERY_CHANGED = "android.intent.action.ADDITIONAL_BATTERY_CHANGED";
    public static final String EXTRA_BATTERY_CURRENT = "batterycurrent";
    public static final String EXTRA_BATTERY_NOTIFY_CODE = "notifycode";
    public static final String EXTRA_BATTERY_REALTIME_CAPATICAL = "realtime_capatical";
    public static final String EXTRA_BATTERY_SOC_JUMP = "soc_jump";
    public static final String EXTRA_CHARGER_TECHNOLOGY = "chargertechnology";
    public static final String EXTRA_CHARGER_VOLTAGE = "chargervoltage";
    public static final String EXTRA_CHARGE_FAST_CHARGER = "chargefastcharger";
    public static final String EXTRA_CHARGE_PLUGGED = "chargeplugged";
    public static final int FAST_CHARGER_TECHNOLOGY = 1;
    public static final int NORMAL_CHARGER_TECHNOLOGY = 0;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: bogus opcode: 0073 in method: android.os.OppoBatteryManager.<init>():void, dex: 
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:118)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:248)
        	at jadx.core.ProcessClass.process(ProcessClass.java:29)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        Caused by: java.lang.IllegalArgumentException: bogus opcode: 0073
        	at com.android.dx.io.OpcodeInfo.get(OpcodeInfo.java:1227)
        	at com.android.dx.io.OpcodeInfo.getName(OpcodeInfo.java:1234)
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:581)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:74)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:104)
        	... 5 more
        */
    public OppoBatteryManager() {
        /*
        // Can't load method instructions: Load method exception: bogus opcode: 0073 in method: android.os.OppoBatteryManager.<init>():void, dex: 
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.OppoBatteryManager.<init>():void");
    }
}
