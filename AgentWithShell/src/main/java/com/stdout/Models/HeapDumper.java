package com.stdout.Models;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.PlatformManagedObject;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HeapDumper {

    protected class Dumper {
        private Object diagnosticMXBean;
        private Method dumpHeapMethod;

        @SuppressWarnings("unchecked")
        protected Dumper() throws Exception {
            Class<?> diagnosticMXBeanClass = Class.forName("com.sun.management.HotSpotDiagnosticMXBean");
            this.diagnosticMXBean = ManagementFactory
                    .getPlatformMXBean((Class<PlatformManagedObject>) diagnosticMXBeanClass);
            this.dumpHeapMethod = diagnosticMXBeanClass.getDeclaredMethod("dumpHeap", String.class, boolean.class);
        }

        public void heapDump(File file, boolean live) throws Exception {
            this.dumpHeapMethod.invoke(this.diagnosticMXBean, file.getAbsolutePath(), live);
        }
    }

    private Dumper createDumper() throws Exception {
        return new Dumper();
    }

    private File createTempFile() throws Exception {
        String date = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm").format(LocalDateTime.now());
        File f = File.createTempFile("heapdump" + date, ".hprof");
        f.delete();
        return f;
    }

    public String dumpHeap() throws Exception {
        String result = "";
        try {
            // create tmp file and return it
            Dumper d = this.createDumper();
            File f = createTempFile();
            d.heapDump(f, false);
            result = f.getAbsolutePath();
            return result;
        } catch (java.lang.reflect.InvocationTargetException e) {
            return null;
        }
    }


}
