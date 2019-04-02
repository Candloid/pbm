REM ********************************************************
REM Add PBM database to SSMS and enable it for Java to use
REM By Bilal Qandeel - 2-Apr-2019
REM ********************************************************

REM Step 1: Enable TCP/IP
set wmiComputer = GetObject( _
    "winmgmts:" _
    & "\\.\root\Microsoft\SqlServer\ComputerManagement10")
set tcpProtocols = wmiComputer.ExecQuery( _
    "select * from ServerNetworkProtocol " _
    & "where InstanceName = 'SQLEXPRESS' and ProtocolName = 'Tcp'")

if tcpProtocols.Count = 1 then
    ' set tcpProtocol = tcpProtocols(0)
    ' I wish this worked, but unfortunately 
    ' there's no int-indexed Item property in this type

    ' Doing this instead
    for each tcpProtocol in tcpProtocols
        dim setEnableResult
            setEnableResult = tcpProtocol.SetEnable()
            if setEnableResult <> 0 then 
                Wscript.Echo "Failed!"
            end if
    next
end if

REM Step 2: Open the right ports in the firewall
netsh firewall set portopening protocol = TCP port = 1433 name = SQLPort mode = ENABLE scope = SUBNET profile = CURRENT

REM Step 3: Modify TCP/IP properties enable a IP address in MS SQL Server
set wmiComputer = GetObject( _
    "winmgmts:" _
    & "\\.\root\Microsoft\SqlServer\ComputerManagement10")
set tcpProperties = wmiComputer.ExecQuery( _
    "select * from ServerNetworkProtocolProperty " _
    & "where InstanceName='SQLEXPRESS' and " _
    & "ProtocolName='Tcp' and IPAddressName='IPAll'")

for each tcpProperty in tcpProperties
    dim setValueResult, requestedValue

    if tcpProperty.PropertyName = "TcpPort" then
        requestedValue = "1433"
    elseif tcpProperty.PropertyName ="TcpDynamicPorts" then
        requestedValue = ""
    end if

    setValueResult = tcpProperty.SetStringValue(requestedValue)
    if setValueResult = 0 then 
        Wscript.Echo "" & tcpProperty.PropertyName & " set."
    else
        Wscript.Echo "" & tcpProperty.PropertyName & " failed!"
    end if
next

REM Step 4: Enable mixed mode authentication in SQL server
REG ADD HKLM\Software\Microsoft\Microsoft SQL Server\MSSQL14.SQLEXPRESS\MSSQLServer /v LoginMode /t REG_DWORD /d 2

REM Step 5: Run the SQL command for the creation of the DB
sqlcmd -i pbm.sql