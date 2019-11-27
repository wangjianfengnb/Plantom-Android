// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: model.proto

package com.phantom.client.model.request;

/**
 * Protobuf type {@code com.phantom.protocol.FetchMessageResponse}
 */
public  final class FetchMessageResponse extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:com.phantom.protocol.FetchMessageResponse)
    FetchMessageResponseOrBuilder {
  // Use FetchMessageResponse.newBuilder() to construct.
  private FetchMessageResponse(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private FetchMessageResponse() {
    isEmpty_ = false;
    messages_ = java.util.Collections.emptyList();
    uid_ = "";
  }

  @Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
  }
  private FetchMessageResponse(
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
          case 8: {

            isEmpty_ = input.readBool();
            break;
          }
          case 18: {
            if (!((mutable_bitField0_ & 0x00000002) == 0x00000002)) {
              messages_ = new java.util.ArrayList<OfflineMessage>();
              mutable_bitField0_ |= 0x00000002;
            }
            messages_.add(
                input.readMessage(OfflineMessage.parser(), extensionRegistry));
            break;
          }
          case 26: {
            String s = input.readStringRequireUtf8();

            uid_ = s;
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
      if (((mutable_bitField0_ & 0x00000002) == 0x00000002)) {
        messages_ = java.util.Collections.unmodifiableList(messages_);
      }
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return Model.internal_static_com_phantom_protocol_FetchMessageResponse_descriptor;
  }

  protected FieldAccessorTable
      internalGetFieldAccessorTable() {
    return Model.internal_static_com_phantom_protocol_FetchMessageResponse_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            FetchMessageResponse.class, Builder.class);
  }

  private int bitField0_;
  public static final int ISEMPTY_FIELD_NUMBER = 1;
  private boolean isEmpty_;
  /**
   * <code>bool isEmpty = 1;</code>
   */
  public boolean getIsEmpty() {
    return isEmpty_;
  }

  public static final int MESSAGES_FIELD_NUMBER = 2;
  private java.util.List<OfflineMessage> messages_;
  /**
   * <code>repeated .com.phantom.protocol.OfflineMessage messages = 2;</code>
   */
  public java.util.List<OfflineMessage> getMessagesList() {
    return messages_;
  }
  /**
   * <code>repeated .com.phantom.protocol.OfflineMessage messages = 2;</code>
   */
  public java.util.List<? extends OfflineMessageOrBuilder>
      getMessagesOrBuilderList() {
    return messages_;
  }
  /**
   * <code>repeated .com.phantom.protocol.OfflineMessage messages = 2;</code>
   */
  public int getMessagesCount() {
    return messages_.size();
  }
  /**
   * <code>repeated .com.phantom.protocol.OfflineMessage messages = 2;</code>
   */
  public OfflineMessage getMessages(int index) {
    return messages_.get(index);
  }
  /**
   * <code>repeated .com.phantom.protocol.OfflineMessage messages = 2;</code>
   */
  public OfflineMessageOrBuilder getMessagesOrBuilder(
      int index) {
    return messages_.get(index);
  }

  public static final int UID_FIELD_NUMBER = 3;
  private volatile Object uid_;
  /**
   * <code>string uid = 3;</code>
   */
  public String getUid() {
    Object ref = uid_;
    if (ref instanceof String) {
      return (String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      String s = bs.toStringUtf8();
      uid_ = s;
      return s;
    }
  }
  /**
   * <code>string uid = 3;</code>
   */
  public com.google.protobuf.ByteString
      getUidBytes() {
    Object ref = uid_;
    if (ref instanceof String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (String) ref);
      uid_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
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
    if (isEmpty_ != false) {
      output.writeBool(1, isEmpty_);
    }
    for (int i = 0; i < messages_.size(); i++) {
      output.writeMessage(2, messages_.get(i));
    }
    if (!getUidBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 3, uid_);
    }
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (isEmpty_ != false) {
      size += com.google.protobuf.CodedOutputStream
        .computeBoolSize(1, isEmpty_);
    }
    for (int i = 0; i < messages_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, messages_.get(i));
    }
    if (!getUidBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, uid_);
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
    if (!(obj instanceof FetchMessageResponse)) {
      return super.equals(obj);
    }
    FetchMessageResponse other = (FetchMessageResponse) obj;

    boolean result = true;
    result = result && (getIsEmpty()
        == other.getIsEmpty());
    result = result && getMessagesList()
        .equals(other.getMessagesList());
    result = result && getUid()
        .equals(other.getUid());
    return result;
  }

  @Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + ISEMPTY_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
        getIsEmpty());
    if (getMessagesCount() > 0) {
      hash = (37 * hash) + MESSAGES_FIELD_NUMBER;
      hash = (53 * hash) + getMessagesList().hashCode();
    }
    hash = (37 * hash) + UID_FIELD_NUMBER;
    hash = (53 * hash) + getUid().hashCode();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static FetchMessageResponse parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static FetchMessageResponse parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static FetchMessageResponse parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static FetchMessageResponse parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static FetchMessageResponse parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static FetchMessageResponse parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static FetchMessageResponse parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static FetchMessageResponse parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static FetchMessageResponse parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static FetchMessageResponse parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static FetchMessageResponse parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static FetchMessageResponse parseFrom(
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
  public static Builder newBuilder(FetchMessageResponse prototype) {
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
   * Protobuf type {@code com.phantom.protocol.FetchMessageResponse}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:com.phantom.protocol.FetchMessageResponse)
          FetchMessageResponseOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return Model.internal_static_com_phantom_protocol_FetchMessageResponse_descriptor;
    }

    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return Model.internal_static_com_phantom_protocol_FetchMessageResponse_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              FetchMessageResponse.class, Builder.class);
    }

    // Construct using com.phantom.client.model.request.FetchMessageResponse.newBuilder()
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
        getMessagesFieldBuilder();
      }
    }
    public Builder clear() {
      super.clear();
      isEmpty_ = false;

      if (messagesBuilder_ == null) {
        messages_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000002);
      } else {
        messagesBuilder_.clear();
      }
      uid_ = "";

      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return Model.internal_static_com_phantom_protocol_FetchMessageResponse_descriptor;
    }

    public FetchMessageResponse getDefaultInstanceForType() {
      return FetchMessageResponse.getDefaultInstance();
    }

    public FetchMessageResponse build() {
      FetchMessageResponse result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public FetchMessageResponse buildPartial() {
      FetchMessageResponse result = new FetchMessageResponse(this);
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      result.isEmpty_ = isEmpty_;
      if (messagesBuilder_ == null) {
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          messages_ = java.util.Collections.unmodifiableList(messages_);
          bitField0_ = (bitField0_ & ~0x00000002);
        }
        result.messages_ = messages_;
      } else {
        result.messages_ = messagesBuilder_.build();
      }
      result.uid_ = uid_;
      result.bitField0_ = to_bitField0_;
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
      if (other instanceof FetchMessageResponse) {
        return mergeFrom((FetchMessageResponse)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(FetchMessageResponse other) {
      if (other == FetchMessageResponse.getDefaultInstance()) return this;
      if (other.getIsEmpty() != false) {
        setIsEmpty(other.getIsEmpty());
      }
      if (messagesBuilder_ == null) {
        if (!other.messages_.isEmpty()) {
          if (messages_.isEmpty()) {
            messages_ = other.messages_;
            bitField0_ = (bitField0_ & ~0x00000002);
          } else {
            ensureMessagesIsMutable();
            messages_.addAll(other.messages_);
          }
          onChanged();
        }
      } else {
        if (!other.messages_.isEmpty()) {
          if (messagesBuilder_.isEmpty()) {
            messagesBuilder_.dispose();
            messagesBuilder_ = null;
            messages_ = other.messages_;
            bitField0_ = (bitField0_ & ~0x00000002);
            messagesBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getMessagesFieldBuilder() : null;
          } else {
            messagesBuilder_.addAllMessages(other.messages_);
          }
        }
      }
      if (!other.getUid().isEmpty()) {
        uid_ = other.uid_;
        onChanged();
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
      FetchMessageResponse parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (FetchMessageResponse) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private boolean isEmpty_ ;
    /**
     * <code>bool isEmpty = 1;</code>
     */
    public boolean getIsEmpty() {
      return isEmpty_;
    }
    /**
     * <code>bool isEmpty = 1;</code>
     */
    public Builder setIsEmpty(boolean value) {
      
      isEmpty_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>bool isEmpty = 1;</code>
     */
    public Builder clearIsEmpty() {
      
      isEmpty_ = false;
      onChanged();
      return this;
    }

    private java.util.List<OfflineMessage> messages_ =
      java.util.Collections.emptyList();
    private void ensureMessagesIsMutable() {
      if (!((bitField0_ & 0x00000002) == 0x00000002)) {
        messages_ = new java.util.ArrayList<OfflineMessage>(messages_);
        bitField0_ |= 0x00000002;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        OfflineMessage, OfflineMessage.Builder, OfflineMessageOrBuilder> messagesBuilder_;

    /**
     * <code>repeated .com.phantom.protocol.OfflineMessage messages = 2;</code>
     */
    public java.util.List<OfflineMessage> getMessagesList() {
      if (messagesBuilder_ == null) {
        return java.util.Collections.unmodifiableList(messages_);
      } else {
        return messagesBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .com.phantom.protocol.OfflineMessage messages = 2;</code>
     */
    public int getMessagesCount() {
      if (messagesBuilder_ == null) {
        return messages_.size();
      } else {
        return messagesBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .com.phantom.protocol.OfflineMessage messages = 2;</code>
     */
    public OfflineMessage getMessages(int index) {
      if (messagesBuilder_ == null) {
        return messages_.get(index);
      } else {
        return messagesBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .com.phantom.protocol.OfflineMessage messages = 2;</code>
     */
    public Builder setMessages(
        int index, OfflineMessage value) {
      if (messagesBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureMessagesIsMutable();
        messages_.set(index, value);
        onChanged();
      } else {
        messagesBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .com.phantom.protocol.OfflineMessage messages = 2;</code>
     */
    public Builder setMessages(
        int index, OfflineMessage.Builder builderForValue) {
      if (messagesBuilder_ == null) {
        ensureMessagesIsMutable();
        messages_.set(index, builderForValue.build());
        onChanged();
      } else {
        messagesBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .com.phantom.protocol.OfflineMessage messages = 2;</code>
     */
    public Builder addMessages(OfflineMessage value) {
      if (messagesBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureMessagesIsMutable();
        messages_.add(value);
        onChanged();
      } else {
        messagesBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .com.phantom.protocol.OfflineMessage messages = 2;</code>
     */
    public Builder addMessages(
        int index, OfflineMessage value) {
      if (messagesBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureMessagesIsMutable();
        messages_.add(index, value);
        onChanged();
      } else {
        messagesBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .com.phantom.protocol.OfflineMessage messages = 2;</code>
     */
    public Builder addMessages(
        OfflineMessage.Builder builderForValue) {
      if (messagesBuilder_ == null) {
        ensureMessagesIsMutable();
        messages_.add(builderForValue.build());
        onChanged();
      } else {
        messagesBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .com.phantom.protocol.OfflineMessage messages = 2;</code>
     */
    public Builder addMessages(
        int index, OfflineMessage.Builder builderForValue) {
      if (messagesBuilder_ == null) {
        ensureMessagesIsMutable();
        messages_.add(index, builderForValue.build());
        onChanged();
      } else {
        messagesBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .com.phantom.protocol.OfflineMessage messages = 2;</code>
     */
    public Builder addAllMessages(
        Iterable<? extends OfflineMessage> values) {
      if (messagesBuilder_ == null) {
        ensureMessagesIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, messages_);
        onChanged();
      } else {
        messagesBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .com.phantom.protocol.OfflineMessage messages = 2;</code>
     */
    public Builder clearMessages() {
      if (messagesBuilder_ == null) {
        messages_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000002);
        onChanged();
      } else {
        messagesBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .com.phantom.protocol.OfflineMessage messages = 2;</code>
     */
    public Builder removeMessages(int index) {
      if (messagesBuilder_ == null) {
        ensureMessagesIsMutable();
        messages_.remove(index);
        onChanged();
      } else {
        messagesBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .com.phantom.protocol.OfflineMessage messages = 2;</code>
     */
    public OfflineMessage.Builder getMessagesBuilder(
        int index) {
      return getMessagesFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .com.phantom.protocol.OfflineMessage messages = 2;</code>
     */
    public OfflineMessageOrBuilder getMessagesOrBuilder(
        int index) {
      if (messagesBuilder_ == null) {
        return messages_.get(index);  } else {
        return messagesBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .com.phantom.protocol.OfflineMessage messages = 2;</code>
     */
    public java.util.List<? extends OfflineMessageOrBuilder>
         getMessagesOrBuilderList() {
      if (messagesBuilder_ != null) {
        return messagesBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(messages_);
      }
    }
    /**
     * <code>repeated .com.phantom.protocol.OfflineMessage messages = 2;</code>
     */
    public OfflineMessage.Builder addMessagesBuilder() {
      return getMessagesFieldBuilder().addBuilder(
          OfflineMessage.getDefaultInstance());
    }
    /**
     * <code>repeated .com.phantom.protocol.OfflineMessage messages = 2;</code>
     */
    public OfflineMessage.Builder addMessagesBuilder(
        int index) {
      return getMessagesFieldBuilder().addBuilder(
          index, OfflineMessage.getDefaultInstance());
    }
    /**
     * <code>repeated .com.phantom.protocol.OfflineMessage messages = 2;</code>
     */
    public java.util.List<OfflineMessage.Builder>
         getMessagesBuilderList() {
      return getMessagesFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        OfflineMessage, OfflineMessage.Builder, OfflineMessageOrBuilder>
        getMessagesFieldBuilder() {
      if (messagesBuilder_ == null) {
        messagesBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            OfflineMessage, OfflineMessage.Builder, OfflineMessageOrBuilder>(
                messages_,
                ((bitField0_ & 0x00000002) == 0x00000002),
                getParentForChildren(),
                isClean());
        messages_ = null;
      }
      return messagesBuilder_;
    }

    private Object uid_ = "";
    /**
     * <code>string uid = 3;</code>
     */
    public String getUid() {
      Object ref = uid_;
      if (!(ref instanceof String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        uid_ = s;
        return s;
      } else {
        return (String) ref;
      }
    }
    /**
     * <code>string uid = 3;</code>
     */
    public com.google.protobuf.ByteString
        getUidBytes() {
      Object ref = uid_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        uid_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string uid = 3;</code>
     */
    public Builder setUid(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      uid_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string uid = 3;</code>
     */
    public Builder clearUid() {
      
      uid_ = getDefaultInstance().getUid();
      onChanged();
      return this;
    }
    /**
     * <code>string uid = 3;</code>
     */
    public Builder setUidBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      uid_ = value;
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


    // @@protoc_insertion_point(builder_scope:com.phantom.protocol.FetchMessageResponse)
  }

  // @@protoc_insertion_point(class_scope:com.phantom.protocol.FetchMessageResponse)
  private static final FetchMessageResponse DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new FetchMessageResponse();
  }

  public static FetchMessageResponse getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<FetchMessageResponse>
      PARSER = new com.google.protobuf.AbstractParser<FetchMessageResponse>() {
    public FetchMessageResponse parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return new FetchMessageResponse(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<FetchMessageResponse> parser() {
    return PARSER;
  }

  @Override
  public com.google.protobuf.Parser<FetchMessageResponse> getParserForType() {
    return PARSER;
  }

  public FetchMessageResponse getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}
