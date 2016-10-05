package org.netcrusher.datagram;

import org.netcrusher.NetCrusher;
import org.netcrusher.core.AbstractCrusherMain;
import org.netcrusher.core.NioReactor;

import java.io.IOException;
import java.net.InetSocketAddress;

public class DatagramCrusherMain extends AbstractCrusherMain {

    @Override
    protected NetCrusher create(NioReactor reactor,
                                InetSocketAddress bindAddress,
                                InetSocketAddress connectAddress) throws IOException
    {
        return DatagramCrusherBuilder.builder()
            .withReactor(reactor)
            .withBindAddress(bindAddress)
            .withConnectAddress(connectAddress)
            .withMaxIdleDurationMs(1000)
            .buildAndOpen();
    }

    @Override
    protected void status(NetCrusher crusher) throws IOException {
        System.out.printf("Datagram crusher for <%s>-<%s>\n", crusher.getBindAddress(), crusher.getConnectAddress());
        super.status(crusher);

        if (crusher.isOpen()) {
            DatagramCrusher datagramCrusher = (DatagramCrusher) crusher;

            System.out.printf("Inner\n");

            System.out.printf("\ttotal read bytes: %d\n",
                datagramCrusher.getInner().getTotalReadBytes());
            System.out.printf("\ttotal read datagrams: %d\n",
                datagramCrusher.getInner().getTotalReadDatagrams());
            System.out.printf("\ttotal sent bytes: %d\n",
                datagramCrusher.getInner().getTotalSentBytes());
            System.out.printf("\ttotal sent datagrams: %d\n",
                datagramCrusher.getInner().getTotalSentDatagrams());

            for (DatagramOuter outer : datagramCrusher.getOuters()) {
                System.out.printf("Outer for <%s>\n", outer.getClientAddress());
                System.out.printf("\ttotal read bytes: %d\n", outer.getTotalReadBytes());
                System.out.printf("\ttotal read datagrams: %d\n", outer.getTotalReadDatagrams());
                System.out.printf("\ttotal sent bytes: %d\n", outer.getTotalSentBytes());
                System.out.printf("\ttotal sent datagrams: %d\n", outer.getTotalSentDatagrams());
                System.out.printf("\tidle duration, ms: %d\n", outer.getIdleDurationMs());
            }
        }
    }

    public static void main(String[] arguments) throws Exception {
        DatagramCrusherMain main = new DatagramCrusherMain();
        main.run(arguments);
    }
}
