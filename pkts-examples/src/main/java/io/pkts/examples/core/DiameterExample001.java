/**
 *
 */
package io.pkts.examples.core;

import io.pkts.PacketHandler;
import io.pkts.Pcap;
import io.pkts.buffer.Buffer;
import io.pkts.diameter.DiameterHeader;
import io.pkts.diameter.avp.FramedAvp;
import io.pkts.framer.DiameterFramer;
import io.pkts.framer.FramingException;
import io.pkts.packet.MACPacket;
import io.pkts.packet.Packet;
import io.pkts.packet.diameter.DiameterPacket;
import io.pkts.packet.sctp.SctpChunk;
import io.pkts.packet.sctp.SctpPacket;
import io.pkts.packet.sctp.impl.SctpDataChunkImpl;
import io.pkts.protocol.Protocol;

import java.io.File;
import java.io.IOException;

/**
 * A very simple example that just loads a pcap and prints out the content of
 * all UDP packets.
 *
 * @author jonas@jonasborjesson.com
 */
public class DiameterExample001 {
    private static final DiameterFramer diameterFramer = new DiameterFramer();

    public static void main(final String[] args) throws IOException, FramingException {

        // Step 1 - The first argument we assume is our file to read.
        final File inputFile = new File(args[0]);

        // Step 2 - obtain a new Pcap instance by supplying an InputStream that points
        //          to a source that contains your captured traffic. Typically you may
        //          have stored that traffic in a file so there are a few convenience
        //          methods for those cases, such as just supplying the name of the
        //          file as shown below.
        final Pcap pcap = Pcap.openStream(inputFile);

        // Step 3 - Once you have obtained an instance, you want to start
        //          looping over the content of the pcap. Do this by calling
        //          the loop function and supply a PacketHandler, which is a
        //          simple interface with only a single method - nextPacket
        pcap.loop(new PacketHandler() {
            @Override
            public boolean nextPacket(final Packet packet) throws IOException {

                // Step 4 - For every new packet the PacketHandler will be
                //          called and you can examine this packet in a few
                //          different ways. You can e.g. check whether the
                //          packet contains a particular protocol, such as UDP
                //          and since we are interested in Diameter, we'll check
                //          for Diameter.
                System.out.println(String.format("has SCTP %s, has DIAMETER %s"
                        , packet.hasProtocol(Protocol.SCTP)
                        , packet.hasProtocol(Protocol.DIAMETER)
                ));
                if (packet.hasProtocol(Protocol.ETHERNET_II)) {
                    MACPacket macPacket = (MACPacket) packet.getPacket(Protocol.ETHERNET_II);
                    System.out.println(String.format("Source: %s, Destination: %s"
                            , macPacket.getSourceMacAddress()
                            , macPacket.getDestinationMacAddress()
                    ));
                }
                // 基于SCTP上的Diameter
                if (packet.hasProtocol(Protocol.SCTP)) {
                    SctpPacket sctpPacket = (SctpPacket) packet.getPacket(Protocol.SCTP);
                    for (SctpChunk chunk : sctpPacket.getChunks()) {
                        System.out.println(String.format("Type: %s", chunk.getType()));
                        switch (chunk.getType()) {
                            case DATA:
                                Buffer userData = ((SctpDataChunkImpl) chunk).getUserData();
                                // 判断是否是Diameter协议
                                if (diameterFramer.accept(userData)) {
                                    DiameterPacket diameterPacket = diameterFramer.frame(sctpPacket, userData);
                                    DiameterHeader diameterHeader = diameterPacket.getHeader();
                                    System.out.println(String.format(
                                            "Avps.size: %s" +
                                                    "，CommandCode: %s，isError: %s" +
                                                    "，isPossiblyRetransmission: %s，isProxiable: %s，isRequest: %s，isResponse: %s"
                                            , diameterPacket.getAllAvps().size()
                                            , diameterHeader.getCommandCode()
                                            , diameterHeader.isError()
                                            , diameterHeader.isPossiblyRetransmission()
                                            , diameterHeader.isProxiable()
                                            , diameterHeader.isRequest()
                                            , diameterHeader.isResponse()
                                    ));
                                    for (FramedAvp framedAvp : diameterPacket.getAllAvps()) {
                                        System.out.println(framedAvp);
                                    }
                                } else {
                                    System.out.println("不知道这个是什么协议！");
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
                if (packet.hasProtocol(Protocol.DIAMETER)) {

                    // Step 5 - Now that we know that the packet contains
                    //          a Diameter packet we get ask to get the Diameter packet
                    //          and once we have it we can examine it in anyway we'd like.
                    final DiameterPacket diameter = (DiameterPacket) packet.getPacket(Protocol.DIAMETER);
                    final DiameterHeader header = diameter.getHeader();
                    System.out.println(header);
                }

                return true;
            }
        });
    }

}
