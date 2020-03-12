//
var mf = Java.type('java.lang.management.ManagementFactory');

function dataHolderPlugin(dataHolder) {
    data.push(mf.getOperatingSystemMXBean().getSystemLoadAverage());
    //data.push(dataHolder.value);
    return '' + JSON.stringify(data);
}