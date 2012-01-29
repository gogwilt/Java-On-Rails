#ActionController::Routing::Routes.draw do |map|
#    match "/test/test_method" => "test#test_method"    
#end

JORController::Routing::Routes.draw({
  "/test/test_method" => "test#test_method"
})