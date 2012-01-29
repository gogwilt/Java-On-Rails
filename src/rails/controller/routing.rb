# Routing mechanism for the Controller Dispatcher.
# @author rbuckheit

module JORController
  
  module Routing 
    
    class Routes
      $routes = Hash.new {|h,k| h[k] = nil}
      
      def self.draw(opts = {})
        $routes = opts
      end
    
      def self.get(route)
        return $routes[route]
      end
    
      def self.dump
        $routes.each{|k,v| puts "#{k} => #{v}"}
      end
    end
    
  end
  
end