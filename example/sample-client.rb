require 'socket'
port = if ARGV[0] then ARGV[0] else nil end
host = if ARGV[1] then ARGV[1] else 'localhost' end
unless port
  warn "[Error] Port number is unset."
  warn "Usage: ruby #{$0} port [hostname]"
  exit
end
sock = TCPSocket.open(host, port)

#Indexing: "{'userId' : 1, 'itemId' : 1}"
#Searching: "{'inputType' : 'user_id', 'userId' : 1, 'howMany' : 5, 'includeKnownItems' : false}"
print "client> "
while msg = STDIN.gets
  msg.chomp!
  if msg == "quit" 
    sock.write("")
    break
  end
  if(msg.size > 0)
    puts "msg: #{msg}, length: #{msg.size}"
    sock.write("#{msg}\n")
    puts("return message: ", sock.gets)
  end
  print "client> "
end
sock.close
