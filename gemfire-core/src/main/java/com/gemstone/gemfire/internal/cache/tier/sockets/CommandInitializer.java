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

package com.gemstone.gemfire.internal.cache.tier.sockets;

import java.util.HashMap;
import java.util.Map;

import com.gemstone.gemfire.internal.cache.tier.Command;
import com.gemstone.gemfire.internal.cache.tier.MessageType;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.AddPdxEnum;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.AddPdxType;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.ClearRegion;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.ClientReady;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.CloseCQ;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.CloseConnection;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.CommitCommand;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.ContainsKey;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.ContainsKey66;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.CreateRegion;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.Destroy;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.Destroy65;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.Destroy70;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.DestroyRegion;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.ExecuteCQ;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.ExecuteCQ61;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.ExecuteFunction;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.ExecuteFunction65;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.ExecuteFunction66;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.ExecuteFunction70;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.ExecuteRegionFunction;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.ExecuteRegionFunction65;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.ExecuteRegionFunction66;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.ExecuteRegionFunctionSingleHop;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.GatewayReceiverCommand;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.Get70;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.GetAll;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.GetAll651;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.GetAll70;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.GetAllForRI;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.GetCQStats;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.GetClientPRMetadataCommand;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.GetClientPRMetadataCommand66;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.GetClientPartitionAttributesCommand;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.GetClientPartitionAttributesCommand66;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.GetDurableCQs;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.GetEntry70;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.GetEntryCommand;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.GetFunctionAttribute;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.GetPDXEnumById;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.GetPDXIdForEnum;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.GetPDXIdForType;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.GetPDXTypeById;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.GetPdxEnums70;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.GetPdxTypes70;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.Invalid;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.Invalidate;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.Invalidate70;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.KeySet;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.MakePrimary;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.MonitorCQ;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.PeriodicAck;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.Ping;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.ProcessBatch;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.Put;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.Put61;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.Put65;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.Put70;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.PutAll;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.PutAll70;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.PutAll75;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.PutUserCredentials;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.Query651;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.RegisterDataSerializers;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.RegisterInstantiators;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.RegisterInterest;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.RegisterInterest61;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.RegisterInterestList;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.RegisterInterestList61;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.RegisterInterestList66;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.RemoveUserAuth;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.Request;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.RequestEventValue;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.RollbackCommand;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.Size;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.StopCQ;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.TXFailoverCommand;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.TXSynchronizationCommand;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.UnregisterInterest;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.UnregisterInterestList;
import com.gemstone.gemfire.internal.cache.tier.sockets.command.UpdateClientNotification;
import com.gemstone.gemfire.internal.shared.Version;

/**
 * A <code>CommandInitializer</code> maintains version specific commands map.
 *
 * @since 5.7
 */

public class CommandInitializer {

  static Map<Version,Map<Integer, Command>> ALL_COMMANDS;
  
  static {
    initializeAllCommands();
  }
  
  private static void initializeAllCommands() {
    ALL_COMMANDS = new HashMap<Version,Map<Integer, Command>>();
    
    // Initialize the GFE 5.7 commands
    Map<Integer, Command> gfe57Commands = new HashMap<Integer, Command>();
    ALL_COMMANDS.put(Version.GFE_57, gfe57Commands);
    gfe57Commands.put(MessageType.PING,Ping.getCommand());
    gfe57Commands.put(MessageType.REQUEST,Request.getCommand());
    gfe57Commands.put(MessageType.PUT,Put.getCommand());
    gfe57Commands.put(MessageType.PUTALL,PutAll.getCommand());
    gfe57Commands.put(MessageType.DESTROY,Destroy.getCommand());
    gfe57Commands.put(MessageType.QUERY,com.gemstone.gemfire.internal.cache.tier.sockets.command.Query.getCommand());
    gfe57Commands.put(MessageType.CLEAR_REGION,ClearRegion.getCommand());
    gfe57Commands.put(MessageType.DESTROY_REGION,DestroyRegion.getCommand());
    gfe57Commands.put(MessageType.REGISTER_INTEREST,RegisterInterest.getCommand());
    gfe57Commands.put(MessageType.UNREGISTER_INTEREST,UnregisterInterest.getCommand());
    gfe57Commands.put(MessageType.REGISTER_INTEREST_LIST,RegisterInterestList.getCommand());
    gfe57Commands.put(MessageType.UNREGISTER_INTEREST_LIST,UnregisterInterestList.getCommand());
    gfe57Commands.put(MessageType.KEY_SET,KeySet.getCommand());
    gfe57Commands.put(MessageType.CONTAINS_KEY,ContainsKey.getCommand());
    gfe57Commands.put(MessageType.CREATE_REGION,CreateRegion.getCommand());
    gfe57Commands.put(MessageType.MAKE_PRIMARY,MakePrimary.getCommand());
    gfe57Commands.put(MessageType.PERIODIC_ACK,PeriodicAck.getCommand());
    gfe57Commands.put(MessageType.REGISTER_INSTANTIATORS,RegisterInstantiators.getCommand());
    gfe57Commands.put(MessageType.PROCESS_BATCH,ProcessBatch.getCommand());
    gfe57Commands.put(MessageType.UPDATE_CLIENT_NOTIFICATION,UpdateClientNotification.getCommand());
    gfe57Commands.put(MessageType.CLOSE_CONNECTION,CloseConnection.getCommand());
    gfe57Commands.put(MessageType.CLIENT_READY,ClientReady.getCommand());
    gfe57Commands.put(MessageType.INVALID,Invalid.getCommand());
    gfe57Commands.put(MessageType.EXECUTECQ_MSG_TYPE,ExecuteCQ.getCommand());
    gfe57Commands.put(MessageType.EXECUTECQ_WITH_IR_MSG_TYPE,ExecuteCQ.getCommand());
    gfe57Commands.put(MessageType.STOPCQ_MSG_TYPE,StopCQ.getCommand());
    gfe57Commands.put(MessageType.CLOSECQ_MSG_TYPE,CloseCQ.getCommand());
    gfe57Commands.put(MessageType.GETCQSTATS_MSG_TYPE,GetCQStats.getCommand());
    gfe57Commands.put(MessageType.MONITORCQ_MSG_TYPE,MonitorCQ.getCommand());
    gfe57Commands.put(MessageType.GET_ALL,GetAll.getCommand());   
       
    // Initialize the GFE 5.8 commands example
    Map<Integer, Command> gfe58Commands = new HashMap<Integer, Command>();
    ALL_COMMANDS.put(Version.GFE_58, gfe58Commands);
    gfe58Commands.putAll(ALL_COMMANDS.get(Version.GFE_57));
    gfe58Commands.put(MessageType.EXECUTE_REGION_FUNCTION,ExecuteRegionFunction.getCommand());
    gfe58Commands.put(MessageType.EXECUTE_FUNCTION,ExecuteFunction.getCommand());

    // Initialize the GFE 6.0.3 commands map
    Map<Integer, Command> gfe603Commands = new HashMap<Integer, Command>();
    gfe603Commands.putAll(ALL_COMMANDS.get(Version.GFE_58));
    ALL_COMMANDS.put(Version.GFE_603, gfe603Commands);

    // Initialize the GFE 6.1 commands
    Map<Integer, Command> gfe61Commands = new HashMap<Integer, Command>();
    ALL_COMMANDS.put(Version.GFE_61, gfe61Commands);
    gfe61Commands.putAll(ALL_COMMANDS.get(Version.GFE_603));
    gfe61Commands.put(MessageType.REGISTER_INTEREST,RegisterInterest61.getCommand());
    gfe61Commands.put(MessageType.REGISTER_INTEREST_LIST,RegisterInterestList61.getCommand());
    gfe61Commands.put(MessageType.EXECUTECQ_MSG_TYPE,ExecuteCQ61.getCommand());
    gfe61Commands.put(MessageType.EXECUTECQ_WITH_IR_MSG_TYPE,ExecuteCQ61.getCommand());
    gfe61Commands.put(MessageType.REQUEST_EVENT_VALUE,RequestEventValue.getCommand());
    gfe61Commands.put(MessageType.PUT,Put61.getCommand());
    gfe61Commands.put(MessageType.REGISTER_DATASERIALIZERS,RegisterDataSerializers.getCommand());

    // Initialize the GFE 6.5 commands
    Map<Integer, Command> gfe65Commands = new HashMap<Integer, Command>();
    ALL_COMMANDS.put(Version.GFE_65, gfe65Commands);
    gfe65Commands.putAll(ALL_COMMANDS.get(Version.GFE_61));
    gfe65Commands.put(MessageType.DESTROY,Destroy65.getCommand());
    gfe65Commands.put(MessageType.PUT,Put65.getCommand());
    gfe65Commands.put(MessageType.EXECUTE_REGION_FUNCTION,ExecuteRegionFunction65.getCommand());
    gfe65Commands.put(MessageType.EXECUTE_FUNCTION,ExecuteFunction65.getCommand());
    gfe65Commands.put(MessageType.GET_CLIENT_PR_METADATA,GetClientPRMetadataCommand.getCommand());
    gfe65Commands.put(MessageType.GET_CLIENT_PARTITION_ATTRIBUTES,GetClientPartitionAttributesCommand.getCommand());
    gfe65Commands.put(MessageType.USER_CREDENTIAL_MESSAGE, PutUserCredentials.getCommand());
    gfe65Commands.put(MessageType.REMOVE_USER_AUTH, RemoveUserAuth.getCommand());
    gfe65Commands.put(MessageType.EXECUTE_REGION_FUNCTION_SINGLE_HOP,ExecuteRegionFunctionSingleHop.getCommand());

    // Initialize the GFE 6.5.1 commands
    Map<Integer, Command> gfe651Commands = new HashMap<Integer, Command>();
    ALL_COMMANDS.put(Version.GFE_651, gfe651Commands);
    gfe651Commands.putAll(ALL_COMMANDS.get(Version.GFE_65));
    gfe651Commands.put(MessageType.QUERY_WITH_PARAMETERS, Query651.getCommand());

    // Initialize the GFE 6.5.1.6 commands
    Map<Integer, Command> gfe6516Commands = new HashMap<Integer, Command>();
    ALL_COMMANDS.put(Version.GFE_6516, gfe6516Commands);
    gfe6516Commands.putAll(ALL_COMMANDS.get(Version.GFE_651));
    gfe6516Commands.put(MessageType.GET_ALL,GetAll651.getCommand());
    gfe6516Commands.put(MessageType.GET_CLIENT_PR_METADATA,GetClientPRMetadataCommand66.getCommand());
    
    // Initialize the GFE 6.6 commands
    Map<Integer, Command> gfe66Commands = new HashMap<Integer, Command>();
    ALL_COMMANDS.put(Version.GFE_66, gfe66Commands);
    gfe66Commands.putAll(ALL_COMMANDS.get(Version.GFE_6516));
    gfe66Commands.put(MessageType.ADD_PDX_TYPE, AddPdxType.getCommand());
    gfe66Commands.put(MessageType.GET_PDX_ID_FOR_TYPE, GetPDXIdForType.getCommand());
    gfe66Commands.put(MessageType.GET_PDX_TYPE_BY_ID, GetPDXTypeById.getCommand());
    gfe66Commands.put(MessageType.SIZE,Size.getCommand());
    gfe66Commands.put(MessageType.INVALIDATE,Invalidate.getCommand());
    gfe66Commands.put(MessageType.COMMIT,CommitCommand.getCommand());
    gfe66Commands.put(MessageType.ROLLBACK, RollbackCommand.getCommand());
    gfe66Commands.put(MessageType.TX_FAILOVER, TXFailoverCommand.getCommand());
    gfe66Commands.put(MessageType.GET_ENTRY, GetEntryCommand.getCommand());
    gfe66Commands.put(MessageType.TX_SYNCHRONIZATION, TXSynchronizationCommand.getCommand());
    gfe66Commands.put(MessageType.GET_CLIENT_PARTITION_ATTRIBUTES, GetClientPartitionAttributesCommand66.getCommand());
    gfe66Commands.put(MessageType.REGISTER_INTEREST_LIST, RegisterInterestList66.getCommand());    
    gfe66Commands.put(MessageType.GET_FUNCTION_ATTRIBUTES, GetFunctionAttribute.getCommand());
    gfe66Commands.put(MessageType.EXECUTE_REGION_FUNCTION, ExecuteRegionFunction66.getCommand());
    gfe66Commands.put(MessageType.EXECUTE_FUNCTION, ExecuteFunction66.getCommand());
    gfe66Commands.put(MessageType.GET_ALL_FOR_RI, GetAllForRI.getCommand());

    //TODO Pushkar Put it into newer version
    gfe66Commands.put(MessageType.GATEWAY_RECEIVER_COMMAND, GatewayReceiverCommand.getCommand());

    gfe66Commands.put(MessageType.CONTAINS_KEY, ContainsKey66.getCommand());

    // Initialize the GFE 6.6.2 commands
    Map<Integer, Command> gfe662Commands = new HashMap<Integer, Command>();
    ALL_COMMANDS.put(Version.GFE_662, gfe662Commands);
    gfe662Commands.putAll(ALL_COMMANDS.get(Version.GFE_66));
    gfe662Commands.put(MessageType.ADD_PDX_ENUM, AddPdxEnum.getCommand());
    gfe662Commands.put(MessageType.GET_PDX_ID_FOR_ENUM, GetPDXIdForEnum.getCommand());
    gfe662Commands.put(MessageType.GET_PDX_ENUM_BY_ID, GetPDXEnumById.getCommand());

    // Initialize the GFE 6.6.2.2 commands (same commands as the GFE 6.6.2 commands)
    // The SERVER_TO_CLIENT_PING message was added, but it doesn't need to be registered here 
    ALL_COMMANDS.put(Version.GFE_6622, gfe662Commands);

    // Initialize the GFE 70 commands
    Map<Integer, Command> gfe70Commands = new HashMap<Integer, Command>();
    ALL_COMMANDS.put(Version.GFE_70, gfe70Commands);
    gfe70Commands.putAll(ALL_COMMANDS.get(Version.GFE_662));
    gfe70Commands.remove(MessageType.GET_ALL_FOR_RI);
    gfe70Commands.put(MessageType.REQUEST, Get70.getCommand());
    gfe70Commands.put(MessageType.GET_ENTRY, GetEntry70.getCommand());
    gfe70Commands.put(MessageType.GET_ALL_70, GetAll70.getCommand());
    gfe70Commands.put(MessageType.PUTALL, PutAll70.getCommand());
    gfe70Commands.put(MessageType.PUT, Put70.getCommand());
    gfe70Commands.put(MessageType.DESTROY, Destroy70.getCommand());
    gfe70Commands.put(MessageType.INVALIDATE, Invalidate70.getCommand());
    gfe70Commands.put(MessageType.GET_PDX_TYPES, GetPdxTypes70.getCommand());
    gfe70Commands.put(MessageType.GET_PDX_ENUMS, GetPdxEnums70.getCommand());    
    gfe70Commands.put(MessageType.EXECUTE_FUNCTION, ExecuteFunction70.getCommand());
    gfe70Commands.put(MessageType.GETDURABLECQS_MSG_TYPE, GetDurableCQs.getCommand());

    Map<Integer, Command> gfe701Commands = new HashMap<Integer, Command>();
    gfe701Commands.putAll(gfe70Commands);
    ALL_COMMANDS.put(Version.GFE_701, gfe701Commands);

    // GFE 71 commands are currently same as GFE 701
    ALL_COMMANDS.put(Version.GFE_71, gfe701Commands);

    Map<Integer, Command> gfe75Commands = new HashMap<Integer, Command>(gfe701Commands);
    // PutAll is changed to chunk responses back to the client
    gfe75Commands.put(MessageType.PUTALL, PutAll75.getCommand());
    ALL_COMMANDS.put(Version.GFE_75, gfe75Commands);
  }

  public static Map<Integer,Command> getCommands(Version version) {
    return ALL_COMMANDS.get(version.getGemFireVersion());
  }

  public static Map<Integer,Command> getCommands(ServerConnection connection) {
    return getCommands(connection.getClientVersion());
  }

  /**
   * A method used by tests for Backward compatibility
   */
  public static void testSetCommands(Map<Integer,Command> testCommands) {
    ALL_COMMANDS.put(Version.TEST_VERSION, testCommands);
  }
}
