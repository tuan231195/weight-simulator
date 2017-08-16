create table TBS_WEIGH_INSTRUCTION
(
	CODE TEXT not null,
	PLATE_NUM NUMERIC(1) not null,
	STEP NUMERIC(2) not null,
	SCALE_1 NUMERIC(6) not null,
	SCALE_1_ACTIVE CHAR not null,
	SCALE_JOIN_1_2 CHAR,
	SCALE_2 NUMERIC(6),
	SCALE_2_ACTIVE CHAR,
	SCALE_JOIN_2_3 CHAR,
	SCALE_3 NUMERIC(6),
	SCALE_3_ACTIVE CHAR,
	SCALE_JOIN_3_4 CHAR,
	SCALE_4 NUMERIC(6),
	SCALE_4_ACTIVE CHAR,
	SCALE_5 NUMERIC(6),
	SCALE_5_ACTIVE CHAR,
	SCALE_JOIN_4_5 CHAR,
	UPDATED DATE default null  not null,
	UPDATED_BY TEXT default null  not null,
	UPDATED_COMMENT TEXT,
	UPDATED_STATUS CHAR
);

create unique index PK_TBSWEIGHINSTRUCTION_CODE
	on TBS_WEIGH_INSTRUCTION (CODE, PLATE_NUM, STEP);


create table vr_vehicle_type
(
	vehicle_type_code char(4),
	vehicle_type_name varchar(255),
	vehicle_cat_code char(10),
	bitmap_name varchar(25),
	conforming char,
	gross integer,
	gross_tolerance integer,
	axle_num integer,
	axlegroup_num integer,
	axlegroup_config integer,
	axlegroup_mass_1 integer,
	axlegroup_mass_2 integer,
	axlegroup_mass_3 integer,
	axlegroup_mass_4 integer,
	axlegroup_mass_5 integer,
	axlegroup_mass_6 integer,
	axlegroup_mass_7 integer,
	axlegroup_space_1_2_min integer,
	axlegroup_space_2_3_min integer,
	axlegroup_space_3_4_min integer,
	axlegroup_space_4_5_min integer,
	axlegroup_space_5_6_min integer,
	axlegroup_space_6_7_min integer,
	axlegroup_space_1_2_max integer,
	axlegroup_space_2_3_max integer,
	axlegroup_space_3_4_max integer,
	axlegroup_space_4_5_max integer,
	axlegroup_space_5_6_max integer,
	axlegroup_space_6_7_max integer,
	axlegroup_dualtyre_1_num integer,
	axlegroup_dualtyre_2_num integer,
	axlegroup_dualtyre_3_num integer,
	axlegroup_dualtyre_4_num integer,
	axlegroup_dualtyre_5_num integer,
	axlegroup_dualtyre_6_num integer,
	axlegroup_dualtyre_7_num integer,
	gross_mass_min integer,
	gross_mass_max integer,
	registered_units integer,
	length_limit integer,
	length_cat integer,
	grp_mass_tol_1 integer,
	grp_mass_tol_2 integer,
	grp_mass_tol_3 integer,
	grp_mass_tol_4 integer,
	grp_mass_tol_5 integer,
	grp_mass_tol_6 integer,
	grp_mass_tol_7 integer,
	active char,
	bitmap_name_numbered varchar(25),
	tab_group_text varchar(30),
	updated date,
	updated_by varchar(15),
	vehicle_type_drive_unit_code char(10),
	picture text,
	screening_priority integer,
	updated_status text,
	picture_numbered text,
	updated_comment text
)
;


-- auto-generated definition
create table vr_vehicle
(
	vehicle_type_code text NOT NULL,
	vehicle_code text PRIMARY KEY,
	vehicle_name text NOT NULL,
	axle1 integer,
	axle2 integer,
	axle3 integer,
	axle4 integer,
	axle5 integer
);