CREATE TABLE `booktyperelation` (
  `IdRelation` varchar(32) NOT NULL COMMENT '主键',
  `IdBook` varchar(32) NOT NULL COMMENT '书籍ID',
  `IdBookType` varchar(32) NOT NULL COMMENT '书籍类型ID',
  `RelationTime` datetime DEFAULT NULL COMMENT '关联时间',
  `remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`IdRelation`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='书籍类型对应关系，一个书籍可以对应多种类型';

CREATE TABLE `booktype` (
  `IdBookType` varchar(32) NOT NULL COMMENT '主键',
  `TypeName` varchar(50) NOT NULL COMMENT '书类名称',
  `AliasName` varchar(45) NOT NULL,
  `TypeAddress` varchar(100) DEFAULT NULL COMMENT '地址：记录未来的网址，或来源的网址',
  `BookNum` int(11) DEFAULT NULL COMMENT '此类书的数量，在书确定类型时更新',
  `ReaderNum` int(11) DEFAULT NULL COMMENT '读者数量：来源分为多种，一种是配置是否关注过其中的书，另一种是阅读过多少章节后进行记录',
  `ParentId` varchar(32) DEFAULT NULL COMMENT '父类别ID，一级类别父类为0',
  `Remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`IdBookType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `chapter` (
  `IDChapter` varchar(32) NOT NULL,
  `oriBookId` int(11) NOT NULL,
  `BookId` varchar(32) DEFAULT NULL COMMENT '书籍ID',
  `Content` mediumtext COMMENT '章节内容',
  `ChapterName` varchar(50) DEFAULT NULL COMMENT '章节名',
  `ChapterOrder` int(11) DEFAULT NULL COMMENT '章节顺序号',
  `ChapterIdNo` int(11) DEFAULT NULL COMMENT '原章节ID号，',
  `ChapterAddress` varchar(100) DEFAULT NULL COMMENT '原章节内容地址',
  `updatetime` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `IsFetchFinish` varchar(45) DEFAULT NULL COMMENT '是否抓取完成，抓取完章节内容，认为完成，完成状态为1，否则为0',
  `remarker` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`IDChapter`),
  KEY `chapter_NO` (`ChapterIdNo`),
  KEY `index_bookid` (`oriBookId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `chaptercontent` (
  `oriBookId` int(11) NOT NULL,
  `chapterNo` int(11) NOT NULL,
  `BookContent` mediumtext,
  PRIMARY KEY (`oriBookId`,`chapterNo`),
  KEY `index_chapterNO` (`oriBookId`,`chapterNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `crawlerlog` (
  `LogId` int(11) NOT NULL AUTO_INCREMENT,
  `CrawlerStartTime` datetime NOT NULL COMMENT '执行开始时间',
  `CrawlerEndTime` datetime NOT NULL COMMENT '执行结束时间',
  `CrawlerUrl` varchar(180) NOT NULL COMMENT '爬行地址',
  `IsFinished` varchar(1) DEFAULT NULL COMMENT '是否完成，主要记录是否完成数据库的记录\n0：未抓取完成\n1：抓取完成，内容抓取成功\n2：内容处理成功',
  `CrawlerMethod` varchar(1) DEFAULT NULL COMMENT '记录执行爬虫的几种方式\n1、手动爬行，\n2、执行任务\n3、',
  `UrlContent` longtext COMMENT '网页全部内容',
  `remark` varchar(4000) DEFAULT NULL,
  PRIMARY KEY (`LogId`),
  KEY `url_index` (`CrawlerUrl`)
) ENGINE=InnoDB AUTO_INCREMENT=1291823 DEFAULT CHARSET=utf8 COMMENT='爬行日志，记录所有爬行网页记录';

CREATE TABLE `netbook` (
  `IdBook` varchar(32) NOT NULL DEFAULT '',
  `BookName` varchar(50) NOT NULL COMMENT '书名',
  `Author` varchar(50) NOT NULL COMMENT '作者',
  `BookAbstract` text COMMENT '摘要',
  `SectionNum` int(11) DEFAULT NULL COMMENT '章节数',
  `AddTime` datetime DEFAULT NULL COMMENT '上架时间',
  `UpdateTime` datetime DEFAULT NULL COMMENT '最后更新时间',
  `CreateTime` timestamp NULL DEFAULT NULL COMMENT '记录创建时间',
  `IsFinish` varchar(1) DEFAULT NULL COMMENT '是否完结，0连载中，1已完结',
  `Cover` blob COMMENT '封面图片',
  `NetBookId` varchar(45) DEFAULT NULL COMMENT '用于记录，网页中书籍的ID',
  `oriTypeName` varchar(100) DEFAULT NULL COMMENT '原始类别名称，抓取数据时存取',
  `Origin` varchar(100) DEFAULT NULL COMMENT '网络来源地址',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`IdBook`),
  KEY `index_bookid` (`NetBookId`),
  KEY `index_oriAdd` (`Origin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='书籍，登记书籍文章';

create table CrawlerTask
(
	TaskId int not null	primary key,
    taskName varchar(100) null comment '任务名称',
    taskType varchar(1) null comment '任务类型,网站、网页、书',
	startDate datetime null,
	endDate datetime null,
	seedUrl varchar(200) null comment '抓取页面连接',
    matchRules varchar(200) null comment '匹配规则',
    tempDir varchar(200) null comment '临时目录',
	threadNum int default 0 null comment '启用的线程数量',
	urlDepth int default -1 null comment '网页抓取深度，目前尚未测出来深度的具体意义，应该是页面的向下跳转页 -1为不控制深度',
	maxNumber int default -1 null comment '默认最大抓取页数，即抓取够多少页后，停止，-1为不限制',
    actualNumber int default 0 null comment '实际数量',
	hasImage varchar(1) null comment '是否取图',
	resume tinyint null comment '是否恢复抓取，还是重新抓取，1为恢复抓取，即继续使用本地库中的种子，进行，0为根据种子重新抓取。',
    taskState varchar(1) null comment '状态，0未开始，1进行中，2已完成',
    remark text null comment '备注'
);