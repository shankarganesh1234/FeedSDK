# FeedSDK

// setting delimiter as tab
awk 'BEGIN {FS="\t"}; {print $1 $5}' 1_EBAY-US_20180701


// tab delimited and filtering on category id
awk 'BEGIN {FS="\t"}; { if ($5 == 29821) { print } }' 1_EBAY-US_20180701


// tab delimited and filtering on seller name
awk 'BEGIN {FS="\t"}; { if ($7 == "fabricdirectwarehouse") { print } }' 1_EBAY-US_20180701
