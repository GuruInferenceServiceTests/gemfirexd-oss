/**
 * Autogenerated by Thrift Compiler (0.9.1)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */

#include <thrift/Thrift.h>
#include <thrift/TApplicationException.h>
#include <thrift/protocol/TProtocol.h>
#include <thrift/transport/TTransport.h>

#include <thrift/cxxfunctional.h>

#include "gfxd_struct_ServiceMetaDataArgs.h"

#include <algorithm>

namespace com { namespace pivotal { namespace gemfirexd { namespace thrift {

const char* ServiceMetaDataArgs::ascii_fingerprint = "E930F6087196ED682390AFBCB3DA75E4";
const uint8_t ServiceMetaDataArgs::binary_fingerprint[16] = {0xE9,0x30,0xF6,0x08,0x71,0x96,0xED,0x68,0x23,0x90,0xAF,0xBC,0xB3,0xDA,0x75,0xE4};

uint32_t ServiceMetaDataArgs::read(::apache::thrift::protocol::TProtocol* iprot) {

  uint32_t xfer = 0;
  std::string fname;
  ::apache::thrift::protocol::TType ftype;
  int16_t fid;

  xfer += iprot->readStructBegin(fname);

  using ::apache::thrift::protocol::TProtocolException;

  bool isset_connId = false;
  bool isset_driverType = false;

  while (true)
  {
    xfer += iprot->readFieldBegin(fname, ftype, fid);
    if (ftype == ::apache::thrift::protocol::T_STOP) {
      break;
    }
    switch (fid)
    {
      case 1:
        if (ftype == ::apache::thrift::protocol::T_I32) {
          xfer += iprot->readI32(this->connId);
          isset_connId = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 2:
        if (ftype == ::apache::thrift::protocol::T_BYTE) {
          xfer += iprot->readByte(this->driverType);
          isset_driverType = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 3:
        if (ftype == ::apache::thrift::protocol::T_STRING) {
          xfer += iprot->readBinary(this->token);
          this->__isset.token = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 4:
        if (ftype == ::apache::thrift::protocol::T_STRING) {
          xfer += iprot->readString(this->schema);
          this->__isset.schema = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 5:
        if (ftype == ::apache::thrift::protocol::T_STRING) {
          xfer += iprot->readString(this->table);
          this->__isset.table = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 6:
        if (ftype == ::apache::thrift::protocol::T_LIST) {
          {
            this->tableTypes.clear();
            uint32_t _size185;
            ::apache::thrift::protocol::TType _etype188;
            xfer += iprot->readListBegin(_etype188, _size185);
            this->tableTypes.resize(_size185);
            uint32_t _i189;
            for (_i189 = 0; _i189 < _size185; ++_i189)
            {
              xfer += iprot->readString(this->tableTypes[_i189]);
            }
            xfer += iprot->readListEnd();
          }
          this->__isset.tableTypes = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 7:
        if (ftype == ::apache::thrift::protocol::T_STRING) {
          xfer += iprot->readString(this->columnName);
          this->__isset.columnName = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 8:
        if (ftype == ::apache::thrift::protocol::T_STRING) {
          xfer += iprot->readString(this->foreignSchema);
          this->__isset.foreignSchema = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 9:
        if (ftype == ::apache::thrift::protocol::T_STRING) {
          xfer += iprot->readString(this->foreignTable);
          this->__isset.foreignTable = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 10:
        if (ftype == ::apache::thrift::protocol::T_STRING) {
          xfer += iprot->readString(this->procedureName);
          this->__isset.procedureName = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 11:
        if (ftype == ::apache::thrift::protocol::T_STRING) {
          xfer += iprot->readString(this->functionName);
          this->__isset.functionName = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 12:
        if (ftype == ::apache::thrift::protocol::T_STRING) {
          xfer += iprot->readString(this->attributeName);
          this->__isset.attributeName = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 13:
        if (ftype == ::apache::thrift::protocol::T_STRING) {
          xfer += iprot->readString(this->typeName);
          this->__isset.typeName = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 14:
        if (ftype == ::apache::thrift::protocol::T_I32) {
          int32_t ecast190;
          xfer += iprot->readI32(ecast190);
          this->typeId = (GFXDType::type)ecast190;
          this->__isset.typeId = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      default:
        xfer += iprot->skip(ftype);
        break;
    }
    xfer += iprot->readFieldEnd();
  }

  xfer += iprot->readStructEnd();

  if (!isset_connId)
    throw TProtocolException(TProtocolException::INVALID_DATA);
  if (!isset_driverType)
    throw TProtocolException(TProtocolException::INVALID_DATA);
  return xfer;
}

uint32_t ServiceMetaDataArgs::write(::apache::thrift::protocol::TProtocol* oprot) const {
  uint32_t xfer = 0;
  xfer += oprot->writeStructBegin("ServiceMetaDataArgs");

  xfer += oprot->writeFieldBegin("connId", ::apache::thrift::protocol::T_I32, 1);
  xfer += oprot->writeI32(this->connId);
  xfer += oprot->writeFieldEnd();

  xfer += oprot->writeFieldBegin("driverType", ::apache::thrift::protocol::T_BYTE, 2);
  xfer += oprot->writeByte(this->driverType);
  xfer += oprot->writeFieldEnd();

  if (this->__isset.token) {
    xfer += oprot->writeFieldBegin("token", ::apache::thrift::protocol::T_STRING, 3);
    xfer += oprot->writeBinary(this->token);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.schema) {
    xfer += oprot->writeFieldBegin("schema", ::apache::thrift::protocol::T_STRING, 4);
    xfer += oprot->writeString(this->schema);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.table) {
    xfer += oprot->writeFieldBegin("table", ::apache::thrift::protocol::T_STRING, 5);
    xfer += oprot->writeString(this->table);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.tableTypes) {
    xfer += oprot->writeFieldBegin("tableTypes", ::apache::thrift::protocol::T_LIST, 6);
    {
      xfer += oprot->writeListBegin(::apache::thrift::protocol::T_STRING, static_cast<uint32_t>(this->tableTypes.size()));
      std::vector<std::string> ::const_iterator _iter191;
      for (_iter191 = this->tableTypes.begin(); _iter191 != this->tableTypes.end(); ++_iter191)
      {
        xfer += oprot->writeString((*_iter191));
      }
      xfer += oprot->writeListEnd();
    }
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.columnName) {
    xfer += oprot->writeFieldBegin("columnName", ::apache::thrift::protocol::T_STRING, 7);
    xfer += oprot->writeString(this->columnName);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.foreignSchema) {
    xfer += oprot->writeFieldBegin("foreignSchema", ::apache::thrift::protocol::T_STRING, 8);
    xfer += oprot->writeString(this->foreignSchema);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.foreignTable) {
    xfer += oprot->writeFieldBegin("foreignTable", ::apache::thrift::protocol::T_STRING, 9);
    xfer += oprot->writeString(this->foreignTable);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.procedureName) {
    xfer += oprot->writeFieldBegin("procedureName", ::apache::thrift::protocol::T_STRING, 10);
    xfer += oprot->writeString(this->procedureName);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.functionName) {
    xfer += oprot->writeFieldBegin("functionName", ::apache::thrift::protocol::T_STRING, 11);
    xfer += oprot->writeString(this->functionName);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.attributeName) {
    xfer += oprot->writeFieldBegin("attributeName", ::apache::thrift::protocol::T_STRING, 12);
    xfer += oprot->writeString(this->attributeName);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.typeName) {
    xfer += oprot->writeFieldBegin("typeName", ::apache::thrift::protocol::T_STRING, 13);
    xfer += oprot->writeString(this->typeName);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.typeId) {
    xfer += oprot->writeFieldBegin("typeId", ::apache::thrift::protocol::T_I32, 14);
    xfer += oprot->writeI32((int32_t)this->typeId);
    xfer += oprot->writeFieldEnd();
  }
  xfer += oprot->writeFieldStop();
  xfer += oprot->writeStructEnd();
  return xfer;
}

void swap(ServiceMetaDataArgs &a, ServiceMetaDataArgs &b) {
  using ::std::swap;
  swap(a.connId, b.connId);
  swap(a.driverType, b.driverType);
  swap(a.token, b.token);
  swap(a.schema, b.schema);
  swap(a.table, b.table);
  swap(a.tableTypes, b.tableTypes);
  swap(a.columnName, b.columnName);
  swap(a.foreignSchema, b.foreignSchema);
  swap(a.foreignTable, b.foreignTable);
  swap(a.procedureName, b.procedureName);
  swap(a.functionName, b.functionName);
  swap(a.attributeName, b.attributeName);
  swap(a.typeName, b.typeName);
  swap(a.typeId, b.typeId);
  swap(a.__isset, b.__isset);
}

}}}} // namespace
