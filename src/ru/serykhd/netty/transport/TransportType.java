package ru.serykhd.netty.transport;

import io.netty.channel.ChannelFactory;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;

import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.incubator.channel.uring.IOUringDatagramChannel;
import io.netty.incubator.channel.uring.IOUringEventLoopGroup;
import io.netty.incubator.channel.uring.IOUringServerSocketChannel;
import io.netty.incubator.channel.uring.IOUringSocketChannel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ThreadFactory;
import java.util.function.BiFunction;

@Getter
@RequiredArgsConstructor
public enum TransportType {


	IOUring(io.netty.incubator.channel.uring.IOUring.isAvailable(),
			(factory, threads) -> new IOUringEventLoopGroup(threads, factory),
			IOUringServerSocketChannel::new, IOUringSocketChannel::new, IOUringDatagramChannel::new),

	EPOLL(io.netty.channel.epoll.Epoll.isAvailable(),
			(factory, threads) -> new EpollEventLoopGroup(threads, factory),
			EpollServerSocketChannel::new, EpollSocketChannel::new, EpollDatagramChannel::new),

	NIO(true,
			(factory, threads) -> new NioEventLoopGroup(threads, factory),
			NioServerSocketChannel::new, NioSocketChannel::new, NioDatagramChannel::new);

	private final boolean aviable;
	private final BiFunction<ThreadFactory, Integer, EventLoopGroup> group;
	private final ChannelFactory<? extends ServerChannel> serverSocketChannelFactory;
	private final ChannelFactory<? extends SocketChannel> socketChannelFactory;
	private final ChannelFactory<? extends DatagramChannel> datagramChannelFactory;

	public EventLoopGroup newEventLoopGroup(@NonNull ThreadFactory factory) {
		return newEventLoopGroup(0, factory);
	}

	public EventLoopGroup newEventLoopGroup(int threads, @NonNull ThreadFactory factory) {
		return group.apply(factory, threads);
	}
}
