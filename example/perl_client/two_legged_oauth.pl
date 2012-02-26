#!/usr/bin/perl

use strict;
use warnings;

use Data::Dumper;
use YAML;
use OAuth::Lite;
use OAuth::Lite::Consumer;

my $oauth = OAuth::Lite::Consumer->new(
 consumer_key => 'CONSUMER_KEY',
 consumer_secret => 'CONSUMER_SECRET',
);
print YAML::Dump($oauth->request(
    method => 'GET',
    url => 'http://localhost:8080/2legged/',
));

print YAML::Dump($oauth->request(
    method => 'POST',
    url => 'http://localhost:8080/2legged/post/submit',
    params => {id => 'foo', password => 'bar'}
));

