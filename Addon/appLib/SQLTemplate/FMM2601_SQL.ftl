-- TMP dung de lay ra folderId
WITH RECURSIVE TMP AS ( 
    SELECT
        F1.ID
        , F1.PARENTFOLDERID
        , F1.FOLDERNAME
        , F1.AUTHORITYID
        , F1.DISPORDER
        , F1.CORPID
        , CORP.VIEWSORTORDER
        , ROW_NUMBER() OVER (ORDER BY DISPORDER) AS RN 
    FROM
        RT2_APPLICATION_FOLDER F1
        , HT2_CORPORATION CORP 
    WHERE
        1 = 1 
        AND F1.CORPID = CORP.CORPID
) 
,--TMPFOLDER dung de lay tat ca folder con cua folderId
TMPFOLDER AS ( 
    SELECT
        F2.ID
        , F2.PARENTFOLDERID
        , F2.FOLDERNAME
        , F2.AUTHORITYID
        , F2.DISPORDER
        , F2.CORPID
        , F2.VIEWSORTORDER
        , 1 AS LEVEL
        , ARRAY [RN] AS SORTARRAY 
    FROM
        TMP F2 
    WHERE
        1 = 1 
        AND F2.PARENTFOLDERID IS NULL 
    UNION 
    SELECT
        F2.ID
        , F2.PARENTFOLDERID
        , F2.FOLDERNAME
        , F2.AUTHORITYID
        , F2.DISPORDER
        , F2.CORPID
        , F2.VIEWSORTORDER
        , P.LEVEL + 1 AS LEVEL
        , ARRAY_APPEND(P.SORTARRAY, F2.RN) AS SORTARRAY 
    FROM
        TMP F2
        , TMPFOLDER P 
    WHERE
        P.ID = F2.PARENTFOLDERID
) 
, TMPBASEFORM AS ( 
    SELECT
        WK_BF.BASEFORMNAME
        , DF.DETAILFORMNAME
        , WK_BF.DISPORDER
        , DF.DETAILFORM
        , WK_BF.BASEFORM
        , WK_BF.DisplayFlag
        , WK_BF.appfolderid 
    FROM
        FT2_DETAIL_FORM DF 
        INNER JOIN ( 
            SELECT
                DFR.DETAILFORMID 
            FROM
                FT2_DETAIL_FORM_ROUTE DFR 
            WHERE
                DFR.ROUTEDEFID = '${param.routeDefId}' 
            GROUP BY
                DFR.DETAILFORMID
        ) WK_DFR 
            ON WK_DFR.DETAILFORMID = DF.DETAILFORMID 
        INNER JOIN ( 
            SELECT
                BF.BASEFORM
                , BF.BASEFORMNAME
                , BF.DISPORDER
                , BF.DisplayFlag
                , BF.appfolderid 
            FROM
                FT2_BASE_FORM BF
        ) WK_BF 
            ON WK_BF.BASEFORM = DF.BASEFORM 
    WHERE
        '1' = CASE 
            WHEN EXISTS ( 
                SELECT
                    WDF.DETAILFORMID 
                FROM
                    FT2_DETAIL_FORM WDF 
                WHERE
                    WDF.DETAILFORMID = DF.DETAILFORMID 
                    AND WDF.AVAILABLEDATE = ( 
                        SELECT
                            MAX(WKDF.AVAILABLEDATE) 
                        FROM
                            FT2_DETAIL_FORM_ROUTE WKDFR
                            , FT2_DETAIL_FORM WKDF 
                        WHERE
                            WKDFR.DETAILFORMID = WKDF.DETAILFORMID 
                            AND WKDFR.ROUTEDEFID = '${param.routeDefId}' 
                            AND WKDF.AVAILABLEDATE <= to_date( 
                                to_char(clock_timestamp(), 'YYYY/MM/DD')
                                , 'YYYY/MM/DD'
                            ) 
                            AND WKDF.BASEFORM = WDF.BASEFORM
                    )
            ) 
                THEN '1' 
            WHEN EXISTS ( 
                SELECT
                    WDF.DETAILFORMID 
                FROM
                    FT2_DETAIL_FORM WDF 
                WHERE
                    WDF.DETAILFORMID = DF.DETAILFORMID 
                    AND 0 = ( 
                        SELECT
                            COUNT(WKDF.DETAILFORMID) 
                        FROM
                            FT2_DETAIL_FORM_ROUTE WKDFR
                            , FT2_DETAIL_FORM WKDF 
                        WHERE
                            WKDFR.DETAILFORMID = WKDF.DETAILFORMID 
                            AND WKDFR.ROUTEDEFID = '${param.routeDefId}' 
                            AND WKDF.AVAILABLEDATE <= to_date( 
                                to_char(clock_timestamp(), 'YYYY/MM/DD')
                                , 'YYYY/MM/DD'
                            ) 
                            AND WKDF.BASEFORM = WDF.BASEFORM
                    ) 
                    AND WDF.AVAILABLEDATE = ( 
                        SELECT
                            MAX(WKDF.AVAILABLEDATE) 
                        FROM
                            FT2_DETAIL_FORM_ROUTE WKDFR
                            , FT2_DETAIL_FORM WKDF 
                        WHERE
                            WKDFR.DETAILFORMID = WKDF.DETAILFORMID 
                            AND WKDFR.ROUTEDEFID = '${param.routeDefId}' 
                            AND WKDF.AVAILABLEDATE > to_date( 
                                to_char(clock_timestamp(), 'YYYY/MM/DD')
                                , 'YYYY/MM/DD'
                            ) 
                            AND WKDF.BASEFORM = WDF.BASEFORM
                    )
            ) 
                THEN '1' 
            ELSE '0' 
            END
) 
, FOLDERDATA AS ( 
    SELECT DISTINCT
        F.ID
        , F.PARENTFOLDERID
        , F.FOLDERNAME
        , F.AUTHORITYID
        , LPAD('・', LEVEL - 1, '・') || F.FOLDERNAME AS FOLDERNAMELEVEL
        , LEVEL
        , F.DISPORDER
        , F.CORPID
        , SORTARRAY
        , F.VIEWSORTORDER 
    FROM
        TMPFOLDER F 
        INNER JOIN ZT2_AUTHORITY A 
            ON A.ID = AUTHORITYID 
    ORDER BY
        F.VIEWSORTORDER
        , SORTARRAY
) 
SELECT DISTINCT
    tmpBaseForm.BASEFORMNAME
    , tmpBaseForm.DETAILFORMNAME
    , tmpBaseForm.DISPORDER
    , tmpBaseForm.DETAILFORM
    , tmpBaseForm.BASEFORM
    , tmpBaseForm.AppFolderID
    , folder.SORTARRAY AS SORTARRAY
    , appMatter.listOrder
FROM
    TMPBASEFORM tmpBaseForm 
    INNER JOIN RT2_APPLICATION_MATTER appMatter 
        ON appMatter.AppForm = tmpBaseForm.BASEFORM 
    INNER JOIN FOLDERDATA folder 
        ON tmpBaseForm.AppFolderID = folder.id 
WHERE
    tmpBaseForm.AppFolderID IS NOT NULL 
    AND appMatter.UsageFlag != '0' 
    AND tmpBaseForm.DisplayFlag = '1' 
UNION ALL 
SELECT
    tmpBaseForm.BASEFORMNAME
    , tmpBaseForm.DETAILFORMNAME
    , tmpBaseForm.DISPORDER
    , tmpBaseForm.DETAILFORM
    , tmpBaseForm.BASEFORM
    , tmpBaseForm.AppFolderID
    , NULL AS SORTARRAY 
    , NULL AS LISTORDER
FROM
    TMPBASEFORM tmpBaseForm 
WHERE
    tmpBaseForm.appfolderid IS NULL 
    AND tmpBaseForm.DisplayFlag = '1' 
ORDER BY
    SORTARRAY
    , LISTORDER
    , DISPORDER
    , DETAILFORM