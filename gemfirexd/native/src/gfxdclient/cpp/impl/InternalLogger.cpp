/*
 * Copyright (c) 2010-2015 Pivotal Software, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License. See accompanying
 * LICENSE file.
 */

/**
 *  Created on: 7 Jun 2014
 *      Author: swale
 */

#include "InternalLogger.h"
#include "LogWriter.h"
#include "InternalUtils.h"

using namespace com::pivotal::gemfirexd;
using namespace com::pivotal::gemfirexd::client;
using namespace com::pivotal::gemfirexd::client::impl;

ThreadSafeMap<boost::thread::id, std::string> InternalLogger::s_threadNames;

std::ostream& InternalLogger::printCompact_(std::ostream& os,
    const LogLevel::type logLevel, const char* flag,
    const boost::thread::id tid)
{
  std::ostream* ostr = &os;
  while (true) {
    std::ostream& out = *ostr;
    try {
      const int64_t ticks =
          boost::chrono::high_resolution_clock::now().time_since_epoch().count();
      out << '[' << LogLevel::toString(logLevel) << " TICKS=" << ticks
          << " GFXD:C_" << flag << " tid=0x" << std::hex << tid << std::dec
          << "] ";
      return out;
    } catch (const std::exception& se) {
      if (ostr == &std::cerr) {
        return out;
      }
      std::cerr << "ERROR: failure in logging: " << se << _GFXD_NEWLINE;
      std::cerr << "ERROR: original message: ";
      ostr = &std::cerr;
    } catch (...) {
      if (ostr == &std::cerr) {
        return out;
      }
      std::cerr << "ERROR: unknown failure in logging" _GFXD_NEWLINE_STR;
      std::cerr << "ERROR: original message: ";
      ostr = &std::cerr;
    }
  }
  return os;
}

void InternalLogger::compactLogThreadName(std::ostream& out,
    const boost::thread::id tid)
{
  if (!Utils::supportsThreadNames() || s_threadNames.containsKey(tid)) {
    return;
  }
  std::string tname;
  if (Utils::getCurrentThreadName(NULL, tname)
      && s_threadNames.putIfAbsent(tid, tname)) {
    printCompact_(out, LogLevel::INFO, "THREAD_NAME", tid) << tid << ' '
        << tname << _GFXD_NEWLINE;
  }
}

void InternalLogger::compactHeader(std::ostream& out,
    const boost::thread::id tid, const char* opId, const char* opSql,
    const int32_t sqlId, const bool isStart, const int64_t nanos,
    const int64_t milliTime, const int32_t connId, const std::string& token)
{
  compactLogThreadName(out, tid);
  out << tid << ' ' << opId;
  if (opSql != NULL) {
    out << ' ' << opSql;
  }
  else if (sqlId != thrift::g_gfxd_constants.INVALID_ID) {
    out << " ID=" << sqlId;
  }
  out << ' ' << connId;
  const size_t tokenLen = token.size();
  if (tokenLen > 0) {
    // hex string
    out << '@';
    Utils::toHexString(token.data(), tokenLen, out);
  }
  if (!isStart) {
    out << ' ' << nanos;
  }
  if (milliTime > 0) {
    out << ' ' << milliTime;
  }
}

void InternalLogger::TRACE_COMPACT(const boost::thread::id tid,
    const char* opId, const char* opSql, const int32_t sqlId,
    const bool isStart, const int64_t nanos, const int32_t connId,
    const std::string& token, const std::exception* se, const int64_t milliTime)
{
  std::ostream& out = LogWriter::global().getRawStream();
  compactHeader(out, tid, opId, opSql, sqlId, isStart, nanos, milliTime, connId,
      token);
  if (se != NULL) {
    out << " STACK: " << stack(*se);
  }
  out << _GFXD_NEWLINE;
}

void InternalLogger::TRACE_COMPACT(const boost::thread::id tid,
    const char* opId, const char* opSql, const int32_t sqlId,
    const bool isStart, const int64_t nanos, const int32_t connId,
    const std::string& token, const SQLException* sqle,
    const int64_t milliTime)
{
  std::ostream& out = LogWriter::global().getRawStream();
  compactHeader(out, tid, opId, opSql, sqlId, isStart, nanos, milliTime, connId,
      token);
  if (sqle != NULL) {
    out << " STACK: " << stack(*sqle);
  }
  out << _GFXD_NEWLINE;
}
