// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: model.proto

package com.phantom.common;

public interface C2GMessageResponseOrBuilder extends
    // @@protoc_insertion_point(interface_extends:com.phantom.protocol.C2GMessageResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string senderId = 1;</code>
   */
  String getSenderId();
  /**
   * <code>string senderId = 1;</code>
   */
  com.google.protobuf.ByteString
      getSenderIdBytes();

  /**
   * <code>string groupId = 2;</code>
   */
  String getGroupId();
  /**
   * <code>string groupId = 2;</code>
   */
  com.google.protobuf.ByteString
      getGroupIdBytes();

  /**
   * <code>int64 timestamp = 3;</code>
   */
  long getTimestamp();

  /**
   * <code>int32 status = 4;</code>
   */
  int getStatus();
}
