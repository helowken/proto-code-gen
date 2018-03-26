package org.proto.test;

public class ProtoPayloadCT {
//    private static UserActivityResponse createPayload() {
//        UserActivityResponse resp = new UserActivityResponse();
//        resp.setUid("uid-11");
//
//        Map<String, IpAddressInfo> ipMap = new HashMap<>();
//        for (int i = 0; i < 3; ++i) {
//            org.proto.core.test.payload.IpAddressInfo info = new org.proto.core.test.payload.IpAddressInfo();
//            info.setCounter((long) (i + 1));
//            info.setLatestActiveTime(System.currentTimeMillis() + (i * 1000));
//            ipMap.put("ip-" + i, info);
//        }
//        resp.setIpAddresses(ipMap);
//
//        org.proto.core.test.payload.DeviceInfos deviceInfos = new org.proto.core.test.payload.DeviceInfos();
//        Map<String, String> fpMap = new HashMap<>();
//        for (int i = 0; i < 3; ++i) {
//            fpMap.put("fp-" + i, "deviceId-" + i);
//        }
//        deviceInfos.setFingerprints(fpMap);
//
//        Map<String, org.proto.core.test.payload.DeviceInfo> deviceInfoMap = new HashMap<>();
//        for (int i = 0; i < 3; ++i) {
//            org.proto.core.test.payload.DeviceInfo deviceInfo = new org.proto.core.test.payload.DeviceInfo();
//            deviceInfo.setDeviceName("deviceName-" + i);
//            deviceInfo.setDeviceType("deviceType-" + i);
//            deviceInfo.setLatestActiveTime(System.currentTimeMillis() + i * 1000);
//            deviceInfo.setRecentSessionId("session-" + i);
//            deviceInfo.setFingerprintList(new HashSet<>(Arrays.asList("fp-1", "fp-2", "fp-3")));
//            Map<String, Long> amrCountMap = new HashMap<>();
//            for (int j = 0; j < 2; ++j) {
//                amrCountMap.put("amr-" + j, (long) (j + 1));
//            }
//            deviceInfo.setAmrCounters(amrCountMap);
//            deviceInfoMap.put("deviceId-" + i, deviceInfo);
//        }
//        deviceInfos.setDevices(deviceInfoMap);
//
//        Map<String, String> sessionMap = new HashMap<>();
//        for (int i = 0; i < 3; ++i) {
//            sessionMap.put("session-" + i, "deviceId-" + i);
//        }
//        deviceInfos.setSessions(sessionMap);
//
//        resp.setDeviceInfos(deviceInfos);
//        return resp;
//    }
//
//    private static void configPayload() {
//        Class<?>[] classList = new Class<?>[]{
//                UserActivityResponse.class,
//                org.proto.core.test.payload.DeviceInfo.class,
//                org.proto.core.test.payload.DeviceInfos.class,
//                org.proto.core.test.payload.IpAddressInfo.class,
//        };
//        for (Class<?> clazz : classList) {
//            ProtoSerdesFactory.config(clazz, () ->
//                    ProtoUtils.createWrapper(clazz,
//                            "Proto" + clazz.getSimpleName(),
//                            FieldsCollector.getFieldWrappers(clazz)
//                    )
//            );
//        }
//    }
//
//    @BeforeClass
//    public static void doBeforeClass() {
//        configPayload();
//    }
//
//
//    @Test
//    public void testDeviceInfos() throws Throwable {
//        UserActivityResponse resp = createPayload();
//        DeviceInfos deviceInfos = resp.getDeviceInfos();
//        byte[] bs = PayloadConverter.serialize(deviceInfos);
//        String s1 = deviceInfos.toString();
//        String s2 = PayloadConverter.deserializeDeviceInfos(bs).toString();
//        assertEquals(s1, s2);
//
//        ProtoDeviceInfos protoDeviceInfos = PayloadConverter.convertTo(deviceInfos);
//        s2 = PayloadConverter.convertTo(protoDeviceInfos).toString();
//        assertEquals(s1, s2);
//
//        ProtoSerdes<DeviceInfos> deviceInfosSerdes = ProtoSerdesFactory.getInstance().getSerdes(DeviceInfos.class);
//        bs = deviceInfosSerdes.serialize(deviceInfos);
//        s2 = deviceInfosSerdes.deserialize(bs).toString();
//        assertEquals(s1, s2);
//
//        protoDeviceInfos = (ProtoDeviceInfos) deviceInfosSerdes.toProto(deviceInfos);
//        s2 = deviceInfosSerdes.toPojo(protoDeviceInfos).toString();
//        assertEquals(s1, s2);
//    }
//
//    @Test
//    public void testUserActivityResponse() throws Throwable {
//        UserActivityResponse resp = createPayload();
//        byte[] bs = PayloadConverter.serialize(resp);
//        String s1 = resp.toString();
//        String s2 = PayloadConverter.deserializeUserActivityResponse(bs).toString();
//        assertEquals(s1, s2);
//
//        ProtoUserActivityResponse protoResp = PayloadConverter.convertTo(resp);
//        s2 = PayloadConverter.convertTo(protoResp).toString();
//        assertEquals(s1, s2);
//
//        ProtoSerdes<UserActivityResponse> serdes = ProtoSerdesFactory.getInstance().getSerdes(UserActivityResponse.class);
//        bs = serdes.serialize(resp);
//        s2 = serdes.deserialize(bs).toString();
//        assertEquals(s1, s2);
//
//        protoResp = (ProtoUserActivityResponse) serdes.toProto(resp);
//        s2 = serdes.toPojo(protoResp).toString();
//        assertEquals(s1, s2);
//    }
//
//    @Test
//    public void testDeviceInfo() throws Throwable {
//        UserActivityResponse resp = createPayload();
//        DeviceInfos deviceInfos = resp.getDeviceInfos();
//        DeviceInfo deviceInfo = deviceInfos.getDevices().values().iterator().next();
//        byte[] bs = PayloadConverter.serialize(deviceInfo);
//        String s1 = deviceInfo.toString();
//        String s2 = PayloadConverter.deserializeDeviceInfo(bs).toString();
//        assertEquals(s1, s2);
//
//        ProtoDeviceInfo protoDeviceInfo = PayloadConverter.convertTo(deviceInfo);
//        s2 = PayloadConverter.convertTo(protoDeviceInfo).toString();
//        assertEquals(s1, s2);
//
//        ProtoSerdes<DeviceInfo> deviceInfoSerdes = ProtoSerdesFactory.getInstance().getSerdes(DeviceInfo.class);
//        bs = deviceInfoSerdes.serialize(deviceInfo);
//        s2 = deviceInfoSerdes.deserialize(bs).toString();
//        assertEquals(s1, s2);
//
//        protoDeviceInfo = (ProtoDeviceInfo) deviceInfoSerdes.toProto(deviceInfo);
//        s2 = deviceInfoSerdes.toPojo(protoDeviceInfo).toString();
//        assertEquals(s1, s2);
//    }
//
//    @Test
//    public void testIpAddressInfo() throws Throwable {
//        UserActivityResponse resp = createPayload();
//        IpAddressInfo ipAddressInfo = resp.getIpAddresses().values().iterator().next();
//        byte[] bs = PayloadConverter.serialize(ipAddressInfo);
//        String s1 = ipAddressInfo.toString();
//        String s2 = PayloadConverter.deserializeIpAddressInfo(bs).toString();
//        assertEquals(s1, s2);
//
//        ProtoIpAddressInfo protoIpAddressInfo = PayloadConverter.convertTo(ipAddressInfo);
//        s2 = PayloadConverter.convertTo(protoIpAddressInfo).toString();
//        assertEquals(s1, s2);
//
//        ProtoSerdes<IpAddressInfo> ipAddressInfoSerdes = ProtoSerdesFactory.getInstance().getSerdes(IpAddressInfo.class);
//        bs = ipAddressInfoSerdes.serialize(ipAddressInfo);
//        s2 = ipAddressInfoSerdes.deserialize(bs).toString();
//        assertEquals(s1, s2);
//
//        protoIpAddressInfo = (ProtoIpAddressInfo) ipAddressInfoSerdes.toProto(ipAddressInfo);
//        s2 = ipAddressInfoSerdes.toPojo(protoIpAddressInfo).toString();
//        assertEquals(s1, s2);
//    }
}
