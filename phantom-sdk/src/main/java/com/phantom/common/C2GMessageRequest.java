// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: model.proto

package com.phantom.common;

/**
 * Protobuf type {@code com.phantom.protocol.C2GMessageRequest}
 */
public  final class C2GMessageRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:com.phantom.protocol.C2GMessageRequest)
    C2GMessageRequestOrBuilder {
  // Use C2GMessageRequest.newBuilder() to construct.
  private C2GMessageRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private C2GMessageRequest() {
    senderId_ = "";
    groupId_ = "";
    timestamp_ = 0L;
    content_ = "";
    crc_ = "";
    platform_ = 0;
  }

  @Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
  }
  private C2GMessageRequest(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    int mutable_bitField0_ = 0;
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          default: {
            if (!input.skipField(tag)) {
              done = true;
            }
            break;
          }
          case 10: {
            String s = input.readStringRequireUtf8();

            senderId_ = s;
            break;
          }
          case 18: {
            String s = input.readStringRequireUtf8();

            groupId_ = s;
            break;
          }
          case 24: {

            timestamp_ = input.readInt64();
            break;
          }
          case 34: {
            String s = input.readStringRequireUtf8();

            content_ = s;
            break;
          }
          case 42: {
            String s = input.readStringRequireUtf8();

            crc_ = s;
            break;
          }
          case 48: {

            platform_ = input.readInt32();
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return Model.internal_static_com_phantom_protocol_C2GMessageRequest_descriptor;
  }

  protected FieldAccessorTable
      internalGetFieldAccessorTable() {
    return Model.internal_static_com_phantom_protocol_C2GMessageRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            C2GMessageRequest.class, Builder.class);
  }

  public static final int SENDERID_FIELD_NUMBER = 1;
  private volatile Object senderId_;
  /**
   * <code>string senderId = 1;</code>
   */
  public String getSenderId() {
    Object ref = senderId_;
    if (ref instanceof String) {
      return (String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      String s = bs.toStringUtf8();
      senderId_ = s;
      return s;
    }
  }
  /**
   * <code>string senderId = 1;</code>
   */
  public com.google.protobuf.ByteString
      getSenderIdBytes() {
    Object ref = senderId_;
    if (ref instanceof String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (String) ref);
      senderId_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int GROUPID_FIELD_NUMBER = 2;
  private volatile Object groupId_;
  /**
   * <code>string groupId = 2;</code>
   */
  public String getGroupId() {
    Object ref = groupId_;
    if (ref instanceof String) {
      return (String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      String s = bs.toStringUtf8();
      groupId_ = s;
      return s;
    }
  }
  /**
   * <code>string groupId = 2;</code>
   */
  public com.google.protobuf.ByteString
      getGroupIdBytes() {
    Object ref = groupId_;
    if (ref instanceof String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (String) ref);
      groupId_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int TIMESTAMP_FIELD_NUMBER = 3;
  private long timestamp_;
  /**
   * <code>int64 timestamp = 3;</code>
   */
  public long getTimestamp() {
    return timestamp_;
  }

  public static final int CONTENT_FIELD_NUMBER = 4;
  private volatile Object content_;
  /**
   * <code>string content = 4;</code>
   */
  public String getContent() {
    Object ref = content_;
    if (ref instanceof String) {
      return (String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      String s = bs.toStringUtf8();
      content_ = s;
      return s;
    }
  }
  /**
   * <code>string content = 4;</code>
   */
  public com.google.protobuf.ByteString
      getContentBytes() {
    Object ref = content_;
    if (ref instanceof String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (String) ref);
      content_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int CRC_FIELD_NUMBER = 5;
  private volatile Object crc_;
  /**
   * <code>string crc = 5;</code>
   */
  public String getCrc() {
    Object ref = crc_;
    if (ref instanceof String) {
      return (String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      String s = bs.toStringUtf8();
      crc_ = s;
      return s;
    }
  }
  /**
   * <code>string crc = 5;</code>
   */
  public com.google.protobuf.ByteString
      getCrcBytes() {
    Object ref = crc_;
    if (ref instanceof String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (String) ref);
      crc_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int PLATFORM_FIELD_NUMBER = 6;
  private int platform_;
  /**
   * <code>int32 platform = 6;</code>
   */
  public int getPlatform() {
    return platform_;
  }

  private byte memoizedIsInitialized = -1;
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (!getSenderIdBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, senderId_);
    }
    if (!getGroupIdBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, groupId_);
    }
    if (timestamp_ != 0L) {
      output.writeInt64(3, timestamp_);
    }
    if (!getContentBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 4, content_);
    }
    if (!getCrcBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 5, crc_);
    }
    if (platform_ != 0) {
      output.writeInt32(6, platform_);
    }
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!getSenderIdBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, senderId_);
    }
    if (!getGroupIdBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, groupId_);
    }
    if (timestamp_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(3, timestamp_);
    }
    if (!getContentBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(4, content_);
    }
    if (!getCrcBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(5, crc_);
    }
    if (platform_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(6, platform_);
    }
    memoizedSize = size;
    return size;
  }

  private static final long serialVersionUID = 0L;
  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof C2GMessageRequest)) {
      return super.equals(obj);
    }
    C2GMessageRequest other = (C2GMessageRequest) obj;

    boolean result = true;
    result = result && getSenderId()
        .equals(other.getSenderId());
    result = result && getGroupId()
        .equals(other.getGroupId());
    result = result && (getTimestamp()
        == other.getTimestamp());
    result = result && getContent()
        .equals(other.getContent());
    result = result && getCrc()
        .equals(other.getCrc());
    result = result && (getPlatform()
        == other.getPlatform());
    return result;
  }

  @Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + SENDERID_FIELD_NUMBER;
    hash = (53 * hash) + getSenderId().hashCode();
    hash = (37 * hash) + GROUPID_FIELD_NUMBER;
    hash = (53 * hash) + getGroupId().hashCode();
    hash = (37 * hash) + TIMESTAMP_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getTimestamp());
    hash = (37 * hash) + CONTENT_FIELD_NUMBER;
    hash = (53 * hash) + getContent().hashCode();
    hash = (37 * hash) + CRC_FIELD_NUMBER;
    hash = (53 * hash) + getCrc().hashCode();
    hash = (37 * hash) + PLATFORM_FIELD_NUMBER;
    hash = (53 * hash) + getPlatform();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static C2GMessageRequest parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static C2GMessageRequest parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static C2GMessageRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static C2GMessageRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static C2GMessageRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static C2GMessageRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static C2GMessageRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static C2GMessageRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static C2GMessageRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static C2GMessageRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static C2GMessageRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static C2GMessageRequest parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(C2GMessageRequest prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @Override
  protected Builder newBuilderForType(
      BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code com.phantom.protocol.C2GMessageRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:com.phantom.protocol.C2GMessageRequest)
      com.phantom.common.C2GMessageRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return Model.internal_static_com_phantom_protocol_C2GMessageRequest_descriptor;
    }

    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return Model.internal_static_com_phantom_protocol_C2GMessageRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              C2GMessageRequest.class, Builder.class);
    }

    // Construct using com.phantom.common.C2GMessageRequest.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    public Builder clear() {
      super.clear();
      senderId_ = "";

      groupId_ = "";

      timestamp_ = 0L;

      content_ = "";

      crc_ = "";

      platform_ = 0;

      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return Model.internal_static_com_phantom_protocol_C2GMessageRequest_descriptor;
    }

    public C2GMessageRequest getDefaultInstanceForType() {
      return C2GMessageRequest.getDefaultInstance();
    }

    public C2GMessageRequest build() {
      C2GMessageRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public C2GMessageRequest buildPartial() {
      C2GMessageRequest result = new C2GMessageRequest(this);
      result.senderId_ = senderId_;
      result.groupId_ = groupId_;
      result.timestamp_ = timestamp_;
      result.content_ = content_;
      result.crc_ = crc_;
      result.platform_ = platform_;
      onBuilt();
      return result;
    }

    public Builder clone() {
      return (Builder) super.clone();
    }
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return (Builder) super.setField(field, value);
    }
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return (Builder) super.clearField(field);
    }
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return (Builder) super.clearOneof(oneof);
    }
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, Object value) {
      return (Builder) super.setRepeatedField(field, index, value);
    }
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return (Builder) super.addRepeatedField(field, value);
    }
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof C2GMessageRequest) {
        return mergeFrom((C2GMessageRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(C2GMessageRequest other) {
      if (other == C2GMessageRequest.getDefaultInstance()) return this;
      if (!other.getSenderId().isEmpty()) {
        senderId_ = other.senderId_;
        onChanged();
      }
      if (!other.getGroupId().isEmpty()) {
        groupId_ = other.groupId_;
        onChanged();
      }
      if (other.getTimestamp() != 0L) {
        setTimestamp(other.getTimestamp());
      }
      if (!other.getContent().isEmpty()) {
        content_ = other.content_;
        onChanged();
      }
      if (!other.getCrc().isEmpty()) {
        crc_ = other.crc_;
        onChanged();
      }
      if (other.getPlatform() != 0) {
        setPlatform(other.getPlatform());
      }
      onChanged();
      return this;
    }

    public final boolean isInitialized() {
      return true;
    }

    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      C2GMessageRequest parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (C2GMessageRequest) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private Object senderId_ = "";
    /**
     * <code>string senderId = 1;</code>
     */
    public String getSenderId() {
      Object ref = senderId_;
      if (!(ref instanceof String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        senderId_ = s;
        return s;
      } else {
        return (String) ref;
      }
    }
    /**
     * <code>string senderId = 1;</code>
     */
    public com.google.protobuf.ByteString
        getSenderIdBytes() {
      Object ref = senderId_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        senderId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string senderId = 1;</code>
     */
    public Builder setSenderId(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      senderId_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string senderId = 1;</code>
     */
    public Builder clearSenderId() {
      
      senderId_ = getDefaultInstance().getSenderId();
      onChanged();
      return this;
    }
    /**
     * <code>string senderId = 1;</code>
     */
    public Builder setSenderIdBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      senderId_ = value;
      onChanged();
      return this;
    }

    private Object groupId_ = "";
    /**
     * <code>string groupId = 2;</code>
     */
    public String getGroupId() {
      Object ref = groupId_;
      if (!(ref instanceof String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        groupId_ = s;
        return s;
      } else {
        return (String) ref;
      }
    }
    /**
     * <code>string groupId = 2;</code>
     */
    public com.google.protobuf.ByteString
        getGroupIdBytes() {
      Object ref = groupId_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        groupId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string groupId = 2;</code>
     */
    public Builder setGroupId(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      groupId_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string groupId = 2;</code>
     */
    public Builder clearGroupId() {
      
      groupId_ = getDefaultInstance().getGroupId();
      onChanged();
      return this;
    }
    /**
     * <code>string groupId = 2;</code>
     */
    public Builder setGroupIdBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      groupId_ = value;
      onChanged();
      return this;
    }

    private long timestamp_ ;
    /**
     * <code>int64 timestamp = 3;</code>
     */
    public long getTimestamp() {
      return timestamp_;
    }
    /**
     * <code>int64 timestamp = 3;</code>
     */
    public Builder setTimestamp(long value) {
      
      timestamp_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int64 timestamp = 3;</code>
     */
    public Builder clearTimestamp() {
      
      timestamp_ = 0L;
      onChanged();
      return this;
    }

    private Object content_ = "";
    /**
     * <code>string content = 4;</code>
     */
    public String getContent() {
      Object ref = content_;
      if (!(ref instanceof String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        content_ = s;
        return s;
      } else {
        return (String) ref;
      }
    }
    /**
     * <code>string content = 4;</code>
     */
    public com.google.protobuf.ByteString
        getContentBytes() {
      Object ref = content_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        content_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string content = 4;</code>
     */
    public Builder setContent(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      content_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string content = 4;</code>
     */
    public Builder clearContent() {
      
      content_ = getDefaultInstance().getContent();
      onChanged();
      return this;
    }
    /**
     * <code>string content = 4;</code>
     */
    public Builder setContentBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      content_ = value;
      onChanged();
      return this;
    }

    private Object crc_ = "";
    /**
     * <code>string crc = 5;</code>
     */
    public String getCrc() {
      Object ref = crc_;
      if (!(ref instanceof String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        crc_ = s;
        return s;
      } else {
        return (String) ref;
      }
    }
    /**
     * <code>string crc = 5;</code>
     */
    public com.google.protobuf.ByteString
        getCrcBytes() {
      Object ref = crc_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        crc_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string crc = 5;</code>
     */
    public Builder setCrc(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      crc_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string crc = 5;</code>
     */
    public Builder clearCrc() {
      
      crc_ = getDefaultInstance().getCrc();
      onChanged();
      return this;
    }
    /**
     * <code>string crc = 5;</code>
     */
    public Builder setCrcBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      crc_ = value;
      onChanged();
      return this;
    }

    private int platform_ ;
    /**
     * <code>int32 platform = 6;</code>
     */
    public int getPlatform() {
      return platform_;
    }
    /**
     * <code>int32 platform = 6;</code>
     */
    public Builder setPlatform(int value) {
      
      platform_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int32 platform = 6;</code>
     */
    public Builder clearPlatform() {
      
      platform_ = 0;
      onChanged();
      return this;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }


    // @@protoc_insertion_point(builder_scope:com.phantom.protocol.C2GMessageRequest)
  }

  // @@protoc_insertion_point(class_scope:com.phantom.protocol.C2GMessageRequest)
  private static final C2GMessageRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new C2GMessageRequest();
  }

  public static C2GMessageRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<C2GMessageRequest>
      PARSER = new com.google.protobuf.AbstractParser<C2GMessageRequest>() {
    public C2GMessageRequest parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return new C2GMessageRequest(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<C2GMessageRequest> parser() {
    return PARSER;
  }

  @Override
  public com.google.protobuf.Parser<C2GMessageRequest> getParserForType() {
    return PARSER;
  }

  public C2GMessageRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

