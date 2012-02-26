#!/usr/bin/perl

use strict;
use warnings;

use YAML;
use OAuth::Lite;
use OAuth::Lite::Consumer;

my $oauth = OAuth::Lite::Consumer->new(
  consumer_key => 'CONSUMER_KEY',
  consumer_secret => 'CONSUMER_SECRET',
);
my $token = OAuth::Lite::Token->new(
  token => 'TOKEN',
  secret => 'TOKEN_SECRET',
);
print YAML::Dump($oauth->request(
    token => $token,
    method => 'GET',
    url => 'http://localhost:8080/',
));

print YAML::Dump($oauth->request(
    token => $token,
    method => 'POST',
    url => 'http://localhost:8080/post/submit',
    params => {id => 'foo', password => 'bar'}
));

