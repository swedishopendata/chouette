-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

INSERT INTO dbo.Line (Id, IsFromDataSourceId, Gid, Number, Name, Designation, DefaultTransportModeCode, IsDefinedByTransportAuthorityId, Monitored, ExistsFromDate, ExistsUpToDate) VALUES (33010000000570941, 3, 9011003000100000, 1, '', '1', 'BUS', 33010000000001407, 0, '2015-05-21 00:00:00', null);

INSERT INTO dbo.DirectionOfLine (Id, IsFromDataSourceId, Gid, DirectionCode, Name, IsOnLineId, ExistsFromDate, ExistsUpToDate, DescriptionNote) VALUES (33010000000570946, 3, 9014003000110000, 'ODD', 'Ringlinjen', 33010000000570941,'2015-05-21 00:00:00', null, '');
INSERT INTO dbo.DirectionOfLine (Id, IsFromDataSourceId, Gid, DirectionCode, Name, IsOnLineId, ExistsFromDate, ExistsUpToDate, DescriptionNote) VALUES (33010000000570951, 3, 9014003000120000, 'EVEN', 'Ringlinjen', 33010000000570941,'2015-05-21 00:00:00', null, '');

INSERT INTO dbo.TransportAuthority (Id, Gid, IsFromDataSourceId, Number, TimeTableReleaseForPublicUseUptoDate, ExistsFromDate, ExistsUptoDate, Code, Name, FormalName) VALUES (33010000000001407, 9010003000000000, 3, 3, '2016-12-31 00:00:00', '2015-05-19 00:00:00', null, 'UL', 'UL', 'Kollektivtrafikförvaltningen, UL');

INSERT INTO dbo.StopArea (Id, IsFromDataSourceId, Gid, Number, Name, ShortName, TypeCode, IsDefinedByTransportAuthorityId, CoordinateSystemName, CentroidNorthingCoordinate, CentroidEastingCoordinate, DefaultInterchangeDurationSeconds, InterchangePriority, ExistsFromDate, ExistsUptoDate) VALUES (33010000045947312, 3, 9021003719000000, 719000, 'Älvkarleby station (Älvkarleby)', 'Älvkarleby stati', 'BUSTERM', 33010000000001407, 'WGS84', '60.557311', '17.426339', 120, 14, '2017-04-20 00:00:00', null);
INSERT INTO dbo.StopArea (Id, IsFromDataSourceId, Gid, Number, Name, ShortName, TypeCode, IsDefinedByTransportAuthorityId, CoordinateSystemName, CentroidNorthingCoordinate, CentroidEastingCoordinate, DefaultInterchangeDurationSeconds, InterchangePriority, ExistsFromDate, ExistsUptoDate) VALUES (33010000045947350, 3, 9021003719007000, 719007, 'Älvkarleby kraftverk (Älvkarleby)', 'Älvkarleby kraft', 'BUSTERM', 33010000000001407, 'WGS84', '60.560902', '17.444006', 300, 13, '2017-04-20 00:00:00', null);

