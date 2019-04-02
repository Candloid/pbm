USE [master]
GO

/****** Object:  Database [pbm]    Script Date: 4/2/2019 1:33:14 AM ******/
CREATE DATABASE [pbm]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'pbm', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL14.SQLEXPRESS\MSSQL\DATA\pbm.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'pbm_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL14.SQLEXPRESS\MSSQL\DATA\pbm_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
GO

ALTER DATABASE [pbm] SET COMPATIBILITY_LEVEL = 140
GO

IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [pbm].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO

ALTER DATABASE [pbm] SET ANSI_NULL_DEFAULT OFF 
GO

ALTER DATABASE [pbm] SET ANSI_NULLS OFF 
GO

ALTER DATABASE [pbm] SET ANSI_PADDING OFF 
GO

ALTER DATABASE [pbm] SET ANSI_WARNINGS OFF 
GO

ALTER DATABASE [pbm] SET ARITHABORT OFF 
GO

ALTER DATABASE [pbm] SET AUTO_CLOSE ON 
GO

ALTER DATABASE [pbm] SET AUTO_SHRINK OFF 
GO

ALTER DATABASE [pbm] SET AUTO_UPDATE_STATISTICS ON 
GO

ALTER DATABASE [pbm] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO

ALTER DATABASE [pbm] SET CURSOR_DEFAULT  GLOBAL 
GO

ALTER DATABASE [pbm] SET CONCAT_NULL_YIELDS_NULL OFF 
GO

ALTER DATABASE [pbm] SET NUMERIC_ROUNDABORT OFF 
GO

ALTER DATABASE [pbm] SET QUOTED_IDENTIFIER OFF 
GO

ALTER DATABASE [pbm] SET RECURSIVE_TRIGGERS OFF 
GO

ALTER DATABASE [pbm] SET  ENABLE_BROKER 
GO

ALTER DATABASE [pbm] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO

ALTER DATABASE [pbm] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO

ALTER DATABASE [pbm] SET TRUSTWORTHY OFF 
GO

ALTER DATABASE [pbm] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO

ALTER DATABASE [pbm] SET PARAMETERIZATION SIMPLE 
GO

ALTER DATABASE [pbm] SET READ_COMMITTED_SNAPSHOT OFF 
GO

ALTER DATABASE [pbm] SET HONOR_BROKER_PRIORITY OFF 
GO

ALTER DATABASE [pbm] SET RECOVERY SIMPLE 
GO

ALTER DATABASE [pbm] SET  MULTI_USER 
GO

ALTER DATABASE [pbm] SET PAGE_VERIFY CHECKSUM  
GO

ALTER DATABASE [pbm] SET DB_CHAINING OFF 
GO

ALTER DATABASE [pbm] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO

ALTER DATABASE [pbm] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO

ALTER DATABASE [pbm] SET DELAYED_DURABILITY = DISABLED 
GO

ALTER DATABASE [pbm] SET QUERY_STORE = OFF
GO

ALTER DATABASE [pbm] SET  READ_WRITE 
GO

USE [master]
GO

/* For security reasons the login is created disabled and with a random password. */
/****** Object:  Login [javaUser]    Script Date: 4/2/2019 1:34:43 AM ******/
CREATE LOGIN [javaUser] WITH PASSWORD=N'0XkFyOWbDXNuk+V6yXht8zISOLYCY/RTmrl50h1LB4s=', DEFAULT_DATABASE=[pbm], DEFAULT_LANGUAGE=[us_english], CHECK_EXPIRATION=ON, CHECK_POLICY=ON
GO

ALTER LOGIN [javaUser] DISABLE
GO

ALTER SERVER ROLE [sysadmin] ADD MEMBER [javaUser]
GO

USE [pbm]
GO

/****** Object:  Table [dbo].[mainRecords]    Script Date: 4/2/2019 1:33:38 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[mainRecords](
	[recordId] [int] NOT NULL,
	[amount] [float] NULL,
	[paid] [bit] NULL,
	[paidDate] [datetime] NULL,
	[expenseType] [int] NULL,
	[paymentType] [int] NULL,
	[repetitionInterval] [int] NULL,
	[retailerName] [text] NULL,
	[retailerLocation] [text] NULL,
	[operationDate] [datetime] NULL,
	[otherDetails] [text] NULL,
 CONSTRAINT [PK_expensesRecords] PRIMARY KEY CLUSTERED 
(
	[recordId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO

USE [pbm]
GO

/****** Object:  Table [dbo].[subRecords]    Script Date: 4/2/2019 1:33:46 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[subRecords](
	[subRecordId] [float] NOT NULL,
	[recordId] [int] NULL,
	[amount] [float] NULL,
	[paid] [bit] NULL,
	[paidDate] [datetime] NULL,
	[expenseType] [int] NULL,
	[paymentType] [int] NULL,
	[repetitionInterval] [int] NULL,
	[retailerName] [text] NULL,
	[retailerLocation] [text] NULL,
	[operationDate] [datetime] NULL,
	[otherDetails] [text] NULL,
 CONSTRAINT [PK_subExpenses] PRIMARY KEY CLUSTERED 
(
	[subRecordId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO