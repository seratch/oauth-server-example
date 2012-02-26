#!/usr/bin/env ruby

require 'rubygems'
require 'net/http'
require 'oauth'

consumer = OAuth::Consumer.new('CONSUMER_KEY', 'CONSUMER_SECRET')
token = OAuth::AccessToken.new(consumer, 'TOKEN', 'TOKEN_SECRET')

http = Net::HTTP.new('localhost', 8080)

# GET expected 401
req = Net::HTTP::Get.new('/')
res = http.request(req)
puts res.code
puts res.body

# GET expected 200
http = Net::HTTP.new('localhost', 8080)
req = Net::HTTP::Get.new('/?foo=bar')
req.oauth!(http, consumer, token)
res = http.request(req)
puts res.code
puts res.body

# POST expected 401
req = Net::HTTP::Post.new('/post/submit')
req.set_form_data({:id => :foo, :password => :bar})
res = http.request(req)
puts res.code
puts res.body

# POST expected 400
req = Net::HTTP::Post.new('/post/submit')
req.set_form_data({:id => :foo})
req.oauth!(http, consumer, token)
res = http.request(req)
puts res.code
puts res.body

# POST expected 200
req = Net::HTTP::Post.new('/post/submit')
req.set_form_data({:id => :foo, :password => :bar})
req.oauth!(http, consumer, token)
res = http.request(req)
puts res.code
puts res.body



